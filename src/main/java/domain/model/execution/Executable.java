package domain.model.execution;

import domain.shared.DomainEvent;
import domain.shared.EventHistory;

public abstract class Executable {

	public static class StepExecutionRequestedEvent extends DomainEvent {
		public final Executable step;

		public StepExecutionRequestedEvent(Executable step) {
			this.step = step;
		}

		public boolean sameEventAs(DomainEvent event) {
			return (event instanceof StepExecutionRequestedEvent && ((StepExecutionRequestedEvent) event).step
					.equals(step)) ? true : false;
		}
	}

	public static class StepExecutedEvent extends DomainEvent {
		public final Executable step;

		public StepExecutedEvent(Executable step) {
			this.step = step;
		}

		public boolean sameEventAs(DomainEvent event) {
			return (event instanceof StepExecutedEvent && ((StepExecutedEvent) event).step
					.equals(step)) ? true : false;
		}
	}

	public static class StepExecutionFailedEvent extends DomainEvent {
		public final Executable step;

		public StepExecutionFailedEvent(Executable step) {
			this.step = step;
		}

		public boolean sameEventAs(DomainEvent event) {
			return (event instanceof StepExecutionFailedEvent && ((StepExecutionFailedEvent) event).step
					.equals(step)) ? true : false;
		}
	}

	public abstract ExitCode execute();

	public abstract void rollback();

	public abstract void resume(EventHistory history);
	
	public String toString(){
		return this.getClass().getSimpleName();
	}

}
