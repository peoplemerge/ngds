package domain.shared;


public interface DomainSubscriber<K extends DomainEvent<?>> extends TypedSubscriber {

	public void handle(K a);
}
