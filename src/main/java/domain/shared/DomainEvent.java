package domain.shared;

public abstract class DomainEvent<K> extends TypedEvent {
	  public abstract boolean sameEventAs(K other);
}
