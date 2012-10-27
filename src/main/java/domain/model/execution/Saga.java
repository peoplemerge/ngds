package domain.model.execution;

import domain.shared.EventHistory;
import domain.shared.EventPublisher;

public class Saga extends Executable {
	
	private Executable step;
	private EventPublisher publisher;
	public Saga(EventPublisher publisher, Executable step) {
		this.step = step;
		this.publisher = publisher;
	}

	@Override
	public ExitCode execute() {
		return step.execute();
	}

	@Override
	public void resume(EventHistory history) {
		step.resume(history);
		
	}

	@Override
	public void rollback() {
		step.rollback();
	}
	
	public String toString(){
		return step.toString();
	}

}
