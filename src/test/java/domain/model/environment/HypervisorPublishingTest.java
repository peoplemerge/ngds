package domain.model.environment;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import domain.shared.EventPublisher;


public class HypervisorPublishingTest {

	
	private EventStore store = mock(EventStore.class);
		
	@Test
	public void hypervisorStores(){
		EventPublisher publisher = new EventPublisher(store);
		Hypervisor.factory(publisher);
		
		Environment environment = new Environment();
		Host first = new Host();
		first.name = "first.example.com";
		environment.hosts.add(first);
		
		Host second = new Host();
		second.name = "second.example.com";
		environment.hosts.add(second);

		
		EnvironmentEvent event = new EnvironmentEvent.Builder(Hypervisor.HypervisorType.REQUESTED, environment ).build();
		publisher.publish(event);
		verify(store,times(1)).store(eq(event));
		//verify(store,times(2)).store(isA(Hypervisor.HostBuilt.class));
		//verify(store,times(1)).store(isA(Hypervisor.AllHostsBuilt.class));
	}
}
