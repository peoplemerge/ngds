package domain.shared;

import java.util.ArrayList;
import java.util.List;

import domain.model.environment.EventStore;

public class EventPublisher {

	private EventStore store;

	public EventPublisher(EventStore store) {
		this.store = store;
	}

	public static interface Subscriber {
		public void handle(DomainEvent event);
	}

	private List<Subscriber> subscribers = new ArrayList<Subscriber>();

	public void addSubscriber(Subscriber subscriber) {
		subscribers.add(subscriber);
	}

	public void publish(DomainEvent event) {
		store.store(event);
		for (Subscriber subscriber : subscribers) {
			subscriber.handle(event);
		}

	}

}
