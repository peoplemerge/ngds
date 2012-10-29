package domain.model.execution;


public abstract class Executable {

	public abstract ExitCode execute();

	public String toString() {
		return this.getClass().getSimpleName();
	}

}
