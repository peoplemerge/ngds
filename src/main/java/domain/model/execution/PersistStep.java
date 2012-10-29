package domain.model.execution;

import domain.model.environment.Environment;
import domain.model.environment.EnvironmentRepository;
import domain.shared.EventPublisher;
import domain.shared.DomainEvent.Type;

public class PersistStep extends Executable {

	private EnvironmentRepository repository;
	private EventPublisher publisher;
	private Environment environment;

	public PersistStep(EnvironmentRepository repository,
			EventPublisher publisher, Environment environment) {
		this.repository = repository;
		this.publisher = publisher;
		this.environment = environment;
	}

	public enum PersistEvent implements Type {
		PERSIST_COMPLETED
	}

	@Override
	public ExitCode execute() {
		repository.save(environment);
		StepExecutionEvent event = new StepExecutionEvent(
				PersistEvent.PERSIST_COMPLETED, this);
		publisher.publish(event);
		return ExitCode.SUCCESS;

	}

}
