package domain.shared;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domain.model.environment.EventStore;

public class EventPublisher {

	private EventStore store;

	public EventPublisher(EventStore store) {
		this.store = store;
	}

	public static interface Subscriber<DomainEvent> {
		public void handle(DomainEvent event);
	}

	private Map<Class<?>, List<Subscriber<DomainEvent>>> subscribers = new HashMap<Class<?>, List<Subscriber<DomainEvent>>>();

	public void addSubscriber(Subscriber<DomainEvent> subscriber) {
		Class<?> type = subscriber.getClass();
		if (!subscribers.containsKey(type)) {
			List<Subscriber<DomainEvent>> list = new ArrayList<Subscriber<DomainEvent>>();
			list.add(subscriber);
			subscribers.put(type, list);
		} else {
			List<Subscriber<DomainEvent>> list = subscribers
					.get(type);
			list.add(subscriber);
		}
	}

	public void publish(DomainEvent<? extends DomainEvent<?>> event) {
		Class<?> type  = event.getClass();
		store.store(event);
		List<Subscriber<DomainEvent>> subscribersToType = subscribers.get(type);
		for (Subscriber<DomainEvent> subscriber : subscribersToType) {
			subscriber.handle(event);
		}

	}

}
