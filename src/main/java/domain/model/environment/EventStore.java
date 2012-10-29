package domain.model.environment;

import domain.shared.DomainEvent;
import domain.shared.EventHistory;

public interface EventStore {
	public void store(DomainEvent<?> event);
	public EventHistory lookup(String sagaName);
}
