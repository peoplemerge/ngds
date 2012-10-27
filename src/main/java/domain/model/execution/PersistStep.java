package domain.model.execution;

import domain.model.environment.Environment;
import domain.model.environment.EnvironmentRepository;
import domain.shared.EventHistory;
import domain.shared.EventPublisher;

public class PersistStep extends Executable {

	private EnvironmentRepository repository;
	private EventPublisher publisher;
	private Environment environment;
	
	public PersistStep(EnvironmentRepository repository, EventPublisher publisher, Environment environment){
		this.repository = repository;
		this.publisher = publisher;
		this.environment = environment;
	}
	
	@Override
	public ExitCode execute() {
		repository.save(environment);
		StepExecutedEvent event = new StepExecutedEvent(this);
		publisher.publish(event);
		return ExitCode.SUCCESS;
		
	}

	@Override
	public void resume(EventHistory history) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub
		
	}
	
}
