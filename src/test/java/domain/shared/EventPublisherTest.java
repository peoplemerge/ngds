package domain.shared;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import domain.model.environment.Environment;
import domain.model.environment.EventStore;
import domain.model.environment.Host;
import domain.model.environment.Hypervisor;


public class EventPublisherTest {

	
	private EventStore store = mock(EventStore.class);
		
	@Test
	public void subscribes(){
		EventPublisher publisher = new EventPublisher(store);
		Hypervisor.factory(publisher);
		
		Environment environment = new Environment();
		Host first = new Host();
		first.name = "first.example.com";
		environment.hosts.add(first);
		
		Host second = new Host();
		second.name = "second.example.com";
		environment.hosts.add(second);

		
		DomainEvent event = new Hypervisor.HostsRequested(environment);
		publisher.publish(event);
		verify(store,times(1)).store(isA(Hypervisor.HostsRequested.class));
		verify(store,times(2)).store(isA(Hypervisor.HostBuilt.class));
		verify(store,times(1)).store(isA(Hypervisor.AllHostsBuilt.class));
	}
}
