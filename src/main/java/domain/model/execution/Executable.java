package domain.model.execution;

import domain.shared.DomainEvent;
import domain.shared.EventHistory;

public abstract class Executable {

	public static class StepExecutionRequestedEvent extends
			DomainEvent<StepExecutionRequestedEvent> {
		public final Executable step;

		public StepExecutionRequestedEvent(Executable step) {
			this.step = step;
		}

		public boolean sameEventAs(StepExecutionRequestedEvent event) {
			return (((StepExecutionRequestedEvent) event).step.equals(step)) ? true
					: false;
		}
	}

	public static class StepExecutedEvent extends
			DomainEvent<StepExecutedEvent> {
		public final Executable step;

		public StepExecutedEvent(Executable step) {
			this.step = step;
		}

		public boolean sameEventAs(StepExecutedEvent event) {
			return (event.step.equals(step)) ? true : false;
		}
	}

	public static class StepExecutionFailedEvent extends
			DomainEvent<StepExecutionFailedEvent> {
		public final Executable step;

		public StepExecutionFailedEvent(Executable step) {
			this.step = step;
		}

		public boolean sameEventAs(StepExecutionFailedEvent event) {
			return (event.step.equals(step)) ? true : false;
		}
	}

	public abstract ExitCode execute();

	public abstract void rollback();

	public abstract void resume(EventHistory history);

	public String toString() {
		return this.getClass().getSimpleName();
	}

}
