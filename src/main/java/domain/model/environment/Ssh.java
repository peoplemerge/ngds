package domain.model.environment;

import domain.model.execution.BlockingEventStep;
import domain.shared.DomainEvent;
import domain.shared.DomainSubscriber;
import domain.shared.EventPublisher;

public class Ssh implements DomainSubscriber<Ssh.DispatchRequested> {

	public static Ssh factory(EventPublisher publisher) {
		Ssh hypervisor = new Ssh(publisher);
		publisher.addSubscriber(hypervisor, new DispatchRequested(null, null));
		return hypervisor;
	}

	private EventPublisher publisher;

	private Ssh(EventPublisher publisher) {
		this.publisher = publisher;
	}

	public static class DispatchRequested extends
			DomainEvent<DispatchRequested> {
		public final Host host;
		public final String commandToRun;

		public DispatchRequested(Host host, String commandToRun) {
			this.host = host;
			this.commandToRun = commandToRun;
		}

		public boolean sameEventAs(DispatchRequested requested) {
			if (requested.commandToRun.equals(commandToRun)
					&& requested.host.equals(host)) {
				return true;
			}
			return false;
		}

	}

	public static class TaskCompleted extends DomainEvent<TaskCompleted> {
		public final DispatchRequested request;

		public TaskCompleted(DispatchRequested request) {
			this.request = request;
		}

		public boolean sameEventAs(TaskCompleted requested) {
				if (requested.request.sameEventAs(request)) {
					return true;
			}
			return false;
		}
	}

	public void sshToHost(Host host, String command) {
		System.out.println("actually ssh to " + host + " to run " + command);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void handle(DispatchRequested event) {
		sshToHost(event.host, event.commandToRun);
		TaskCompleted done = new TaskCompleted(event);
		publisher.publish(done);
	}

	public BlockingEventStep buildStepFor(Host host, String command) {
		DispatchRequested eventToSend = new DispatchRequested(host, command);
		TaskCompleted waitingFor = new TaskCompleted(eventToSend);

		BlockingEventStep step = BlockingEventStep.factory(publisher,
				eventToSend, waitingFor);
		return step;
	}

}
