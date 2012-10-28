package domain.shared.publisher;


public interface DomainSubscriber extends Subscriber<DomainEventPublisher, DomainSubscriber, TypedEvent> {

	public void handle(TypedEvent a);
}
