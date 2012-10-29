package domain.model.environment;

import domain.model.execution.BlockingEventStep;
import domain.shared.DomainSubscriber;
import domain.shared.EventPublisher;
import domain.shared.DomainEvent.Type;

public class Hypervisor implements DomainSubscriber<EnvironmentEvent> {

	public static Hypervisor factory(EventPublisher publisher) {
		Hypervisor hypervisor = new Hypervisor(publisher);
		publisher.addSubscriber(hypervisor, new EnvironmentEvent.Builder(
				HypervisorType.REQUESTED, null).build());
		return hypervisor;
	}

	private EventPublisher publisher;

	private Hypervisor(EventPublisher publisher) {
		this.publisher = publisher;
	}

	public enum HypervisorType implements Type {
		REQUESTED, HOST_BUILT, ALL_HOSTS_BUILT
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

	public BlockingEventStep buildStepFor(Environment environment) {
		EnvironmentEvent eventToSend = new EnvironmentEvent.Builder(
				HypervisorType.REQUESTED, environment).build();
		EnvironmentEvent waitingFor = new EnvironmentEvent.Builder(
				HypervisorType.ALL_HOSTS_BUILT, environment).build();

		BlockingEventStep step = BlockingEventStep.factory(publisher,
				eventToSend, waitingFor);
		return step;

	}

	public void handle(EnvironmentEvent event) {
		if (event.type == HypervisorType.REQUESTED) {
			for (Host host : event.environment.hosts) {
				provisionHost(host);
				EnvironmentEvent hostEvent = new EnvironmentEvent.Builder(
						HypervisorType.HOST_BUILT, event.environment).withHost(host).build();

				publisher.publish(hostEvent);
			}
			EnvironmentEvent hostsBuiltEvent = new EnvironmentEvent.Builder(
					HypervisorType.ALL_HOSTS_BUILT, event.environment).build();

			publisher.publish(hostsBuiltEvent);
		}
	}

}
