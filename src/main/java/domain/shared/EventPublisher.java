package domain.shared;

import domain.model.environment.EventStore;


public class EventPublisher extends Publisher<EventPublisher, TypedSubscriber, TypedEvent>{

	private EventStore store;

	public EventPublisher(EventStore store){
		this.store = store;
	}

	public void publish(DomainEvent<?> event) {
		super.publish(event);
		store.store(event);
	}

}
