package domain.model.execution;

import java.util.LinkedList;

import domain.shared.EventHistory;
import domain.shared.EventPublisher;

public class SequentialSteps extends Executable {
	
	private final LinkedList<Executable> steps = new LinkedList<Executable>();
	
	private EventPublisher publisher;
	
	public SequentialSteps(EventPublisher publisher) {
		this.publisher = publisher;
	}
	
	public void add(Executable step){
		steps.add(step);
	}
	
	
	@Override
	public ExitCode execute() {
		publisher.publish(new StepExecutionRequestedEvent(this));
		for(Executable step : steps){
			publisher.publish(new StepExecutionRequestedEvent(step));
			ExitCode exitcode = step.execute();
			if(exitcode != ExitCode.SUCCESS){
				publisher.publish(new StepExecutionFailedEvent(step));
				return exitcode;
			}
			publisher.publish(new StepExecutedEvent(step));
		}
		publisher.publish(new StepExecutedEvent(this));
		return ExitCode.SUCCESS;
	}

	@Override
	public void resume(EventHistory history) {
		// TODO Auto-generated method stub
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub
		// should rollback starting with that last executed step
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
