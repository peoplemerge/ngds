package domain.model.execution;

import java.util.LinkedList;

import domain.shared.EventPublisher;
import domain.shared.DomainEvent.Type;

public class SequentialSteps extends Executable {
	
	private final LinkedList<Executable> steps = new LinkedList<Executable>();
	
	private EventPublisher publisher;
	
	public SequentialSteps(EventPublisher publisher) {
		this.publisher = publisher;
	}
	
	public void add(Executable step){
		steps.add(step);
	}
	
	public enum StepType implements Type{
		SEQUENCE_REQUESTED, SEQUENCE_STEP_REQUESTED, SEQUENCE_STEP_EXECUTED, SEQUENCE_EXECUTED, SEQUENTIAL_FAILED;
	}
	
	@Override
	public ExitCode execute() {
		publisher.publish(new StepExecutionEvent(StepType.SEQUENCE_REQUESTED,this));
		for(Executable step : steps){
			publisher.publish(new StepExecutionEvent(StepType.SEQUENCE_STEP_REQUESTED,step));
			ExitCode exitcode = step.execute();
			if(exitcode != ExitCode.SUCCESS){
				publisher.publish(new StepExecutionEvent(StepType.SEQUENTIAL_FAILED,step));
				return exitcode;
			}
			publisher.publish(new StepExecutionEvent(StepType.SEQUENCE_STEP_EXECUTED,step));
		}
		publisher.publish(new StepExecutionEvent(StepType.SEQUENCE_EXECUTED,this));
		return ExitCode.SUCCESS;
	}

	public String toString(){
		String retval = super.toString() + ": \n[\n" ;
		for(Executable step : steps){
			retval += "* " + step.toString() + "\n";
		}
		retval += "]";
		return retval;
	}
}
