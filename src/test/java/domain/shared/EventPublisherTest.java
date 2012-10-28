package domain.shared;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import domain.model.environment.Environment;
import domain.model.environment.EventStore;
import domain.model.environment.Host;
import domain.model.environment.Hypervisor;
import domain.model.environment.Hypervisor.HostsRequested;


public class EventPublisherTest {

	
	private EventStore store = mock(EventStore.class);
		
	private class TestEvent extends DomainEvent<TestEvent>{
		public boolean sameEventAs(TestEvent event) {return false;}
	} 
	
	private class TestSubscriber implements EventPublisher.Subscriber<DomainEvent>{
		
		public boolean handled = false;
		
		public void handle(DomainEvent event) {
			handled = true;			
			
		}
	}
	
	@Test
	public void addSubscriber(){
		EventPublisher publisher = new EventPublisher(store);
		TestSubscriber subscriber = new TestSubscriber();
		
		publisher.addSubscriber(subscriber);
		TestEvent testEvent = new TestEvent();
		publisher.publish(testEvent);
	}
	
	@Test
	public void subscribesHypervisor(){
		EventPublisher publisher = new EventPublisher(store);
		Hypervisor.factory(publisher);
		
		Environment environment = new Environment();
		Host first = new Host();
		first.name = "first.example.com";
		environment.hosts.add(first);
		
		Host second = new Host();
		second.name = "second.example.com";
		environment.hosts.add(second);

		
		HostsRequested event = new Hypervisor.HostsRequested(environment);
		publisher.publish(event);
		verify(store,times(1)).store(isA(Hypervisor.HostsRequested.class));
		verify(store,times(2)).store(isA(Hypervisor.HostBuilt.class));
		verify(store,times(1)).store(isA(Hypervisor.AllHostsBuilt.class));
	}
}
