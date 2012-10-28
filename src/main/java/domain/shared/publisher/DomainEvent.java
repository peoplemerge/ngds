package domain.shared.publisher;

public abstract class DomainEvent<K> extends TypedEvent {
	  public abstract boolean sameEventAs(K other);
}
