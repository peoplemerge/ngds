package application;

import infrastructure.persistence.InMemoryEnvironmentRepository;
import infrastructure.persistence.InMemoryEventStore;
import domain.model.environment.Environment;
import domain.model.environment.EnvironmentRepository;
import domain.model.environment.EventStore;
import domain.model.environment.Host;
import domain.model.environment.Hypervisor;
import domain.model.environment.Ssh;
import domain.model.execution.BlockingEventStep;
import domain.model.execution.ConcurrentSteps;
import domain.model.execution.CreatesSaga;
import domain.model.execution.PersistStep;
import domain.model.execution.Saga;
import domain.model.execution.SequentialSteps;
import domain.shared.EventPublisher;

public class CreateEnvironmentCommand implements CreatesSaga {

	private EventStore eventStore = new InMemoryEventStore();
	private EventPublisher publisher ;
	private EnvironmentRepository repo = new InMemoryEnvironmentRepository();

	private CreateEnvironmentCommand() {
	}

	public static class Builder {
		private CreateEnvironmentCommand command = new CreateEnvironmentCommand();

		public Builder withEventStore(EventStore eventStore) {
			command.eventStore = eventStore;
			return this;
		}
		
		public Builder withEnvironmentRepository(EnvironmentRepository repo){
			command.repo  = repo;
			return this;
		}

		public CreateEnvironmentCommand build() {
			if(command.publisher == null){
				command.publisher = new EventPublisher(command.eventStore);
			}
			return command;
		}
	}

	public Saga create() {
		
		Environment environment = new Environment();
		environment.add(new Host("first.example.com"));
		environment.add(new Host("second.example.com"));

		Hypervisor hypervisor = Hypervisor.factory(publisher);
		Ssh ssh = Ssh.factory(publisher);

		
		SequentialSteps sequence = new SequentialSteps(publisher);
		BlockingEventStep createVms = hypervisor.buildStepFor(environment);
		sequence.add(createVms);

		ConcurrentSteps concurrent = new ConcurrentSteps(publisher);
		for(Host host : environment.hosts){
			BlockingEventStep sshToVms = ssh.buildStepFor(environment, host, "echo hello world");
			concurrent.add(sshToVms);
		}
		sequence.add(concurrent);
		
		PersistStep persistStep = new PersistStep(repo, publisher,environment);
		sequence.add(persistStep);
		
		Saga saga = new Saga(publisher, sequence);

		return saga;
	}

}
