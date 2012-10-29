package domain.model.execution;

import domain.shared.EventHistory;
import domain.shared.EventPublisher;

public class Saga extends AdvancedExecutable {

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
		if (step instanceof AdvancedExecutable) {
			((AdvancedExecutable) step).resume(history);
		}

	}

	@Override
	public void rollback() {
		if (step instanceof AdvancedExecutable) {
			((AdvancedExecutable) step).rollback();
		}
	}

	public String toString() {
		return step.toString();
	}

}
