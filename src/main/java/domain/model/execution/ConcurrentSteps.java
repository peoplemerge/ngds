package domain.model.execution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import domain.model.execution.Executable.StepExecutedEvent;
import domain.model.execution.Executable.StepExecutionRequestedEvent;
import domain.shared.DomainEvent;
import domain.shared.EventHistory;
import domain.shared.EventPublisher;

public class ConcurrentSteps extends Executable {

	private final List<Executable> steps = new ArrayList<Executable>();
	private EventPublisher publisher;

	public ConcurrentSteps(EventPublisher publisher) {
		this.publisher = publisher;
	}
	
	public boolean equals(Object o){
		if (o instanceof ConcurrentSteps){
			ConcurrentSteps other = (ConcurrentSteps) o;
			return other.steps.equals(steps);
		}
		return false;
	}
	
	public static class ConcurrentExecutableRequestedEvent extends DomainEvent{
		public final Executable step;
		public ConcurrentExecutableRequestedEvent(Executable step){
			this.step = step;
		}

		public boolean sameEventAs(DomainEvent event) {
			return (event instanceof ConcurrentExecutableRequestedEvent && ((ConcurrentExecutableRequestedEvent) event).step
					.equals(step)) ? true : false;
		}
	}
	public static class ConcurrentExecutableCompletedEvent extends DomainEvent{
		public final Executable step;
		public ConcurrentExecutableCompletedEvent(Executable step){
			this.step = step;
		}
		public boolean sameEventAs(DomainEvent event) {
			return (event instanceof ConcurrentExecutableCompletedEvent && ((ConcurrentExecutableCompletedEvent) event).step
					.equals(step)) ? true : false;
		}
	}


	public void add(Executable runnable) {
		steps.add(runnable);
	}

	public ExitCode execute() {
		ExecutorService executor = Executors.newCachedThreadPool();
		final Map<Executable,ExitCode> completionCodes = new TreeMap<Executable,ExitCode>();
		for (final Executable step : steps) {
			publisher.publish(new StepExecutionRequestedEvent(step));
			Runnable toRun = new Runnable(){
				public void run() {
					ExitCode code = step.execute();
					completionCodes.put(step,code);
				}
			};
			executor.execute(toRun);
		}
		try {
			executor.shutdown();
			// TODO get this from somewhere nicer
			boolean complete = executor.awaitTermination(1, TimeUnit.HOURS);
			if (complete == false) {
				return ExitCode.FAILURE;
			}
		} catch (InterruptedException e) {
			// TODO think about how to handle user termination better.
			return ExitCode.FAILURE;
		}
		ExitCode retval = ExitCode.SUCCESS;
		for (Executable step : completionCodes.keySet()) {
			ExitCode code = completionCodes.get(step);
			if(code == ExitCode.SUCCESS){
				publisher.publish(new StepExecutedEvent(step));
			} else {
				publisher.publish(new StepExecutionFailedEvent(step));
				retval = code;
			}
		}
		return retval;
	}

	@Override
	public void resume(EventHistory history) {
		// TODO Auto-generated method stub

	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub

	}
	public String toString(){
		String retval = super.toString() + ": \n{\n" ;
		for(Executable step : steps){
			retval += "+ " + step.toString() + "\n";
		}
		retval += "}";
		return retval;
	}

}
