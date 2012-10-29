package domain.model.execution;

import domain.shared.DomainEvent;

public class StepExecutionEvent extends DomainEvent<StepExecutionEvent> {

	public final Executable executable;

	public final Type type;
	public StepExecutionEvent(Type type, Executable executable){
		this.executable = executable;
		this.type = type;
	}

	public boolean sameEventAs(StepExecutionEvent event) {
		return event.executable.equals(event.executable) && event.type == type; 
	}
	
	public String toString(){
		return this.getClass().getSimpleName() + " "+ type + " " + executable + " " + occurred ;
	}

}
