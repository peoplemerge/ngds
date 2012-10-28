package domain.model.environment;

import domain.model.execution.BlockingEventStep;
import domain.shared.DomainEvent;
import domain.shared.EventPublisher;

public class Hypervisor implements EventPublisher.Subscriber {

	public static Hypervisor factory(EventPublisher publisher) {
		Hypervisor hypervisor = new Hypervisor(publisher);
		publisher.addSubscriber(hypervisor);
		return hypervisor;
	}

	private EventPublisher publisher;

	private Hypervisor(EventPublisher publisher) {
		this.publisher = publisher;
	}

	public static class HostsRequested extends DomainEvent<HostsRequested> {
		public final Environment environment;

		public HostsRequested(Environment environment) {
			this.environment = environment;
		}

		public boolean sameEventAs(HostsRequested event) {
			return event.environment.equals(environment);
		}
	}

	public static class HostBuilt extends DomainEvent<HostBuilt> {
		public final Host host;

		public HostBuilt(Host host) {
			this.host = host;
		}

		public boolean sameEventAs(HostBuilt event) {
			return event.host.equals(host);
			
		}
	}

	public static class AllHostsBuilt extends DomainEvent<AllHostsBuilt> {
		public final Environment environment;

		public AllHostsBuilt(HostsRequested source) {
			environment = source.environment;
		}

		public boolean sameEventAs(AllHostsBuilt event) {
			return event.environment.equals(environment);
		}
	}

	public void provisionHost(Host host) {
		// Actually go to VMWare, EC2, etc.
		System.out.println("Hypervisor is provisioning " + host.name);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void handle(DomainEvent recieved) {
		if (recieved instanceof HostsRequested) {
			// don't really care about other types of events.
			HostsRequested event = (HostsRequested) recieved;
			for (Host host : event.environment.hosts) {
				provisionHost(host);
				HostBuilt hostEvent = new HostBuilt(host);
				publisher.publish(hostEvent);
			}
			AllHostsBuilt hostsBuiltEvent = new AllHostsBuilt(event);
			publisher.publish(hostsBuiltEvent);
		}
	}

	public BlockingEventStep buildStepFor(Environment environment) {

		HostsRequested eventToSend = new HostsRequested(environment);
		AllHostsBuilt waitingFor = new AllHostsBuilt(eventToSend);

		BlockingEventStep step = BlockingEventStep.factory(publisher,
				eventToSend, waitingFor);
		return step;

	}

}
