package domain.model.execution;

import domain.shared.EventHistory;

public abstract class AdvancedExecutable extends Executable {
	
	public abstract void rollback();

	public abstract void resume(EventHistory history);

}
