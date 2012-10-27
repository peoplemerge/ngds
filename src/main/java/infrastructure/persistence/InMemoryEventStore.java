package infrastructure.persistence;

import domain.model.environment.EventStore;
import domain.shared.DomainEvent;
import domain.shared.EventHistory;

public class InMemoryEventStore implements EventStore {

	
	private EventHistory history = new EventHistory();
	public EventHistory lookup(String sagaName) {
		return new EventHistory();
	}

	
	public void store(DomainEvent event) {
		history.events.add(event);
	}

	public String toString(){
		return history.toString();
	}
}
