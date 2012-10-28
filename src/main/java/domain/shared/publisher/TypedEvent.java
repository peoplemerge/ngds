package domain.shared.publisher;

import java.util.Date;

public abstract class TypedEvent extends Event<DomainEventPublisher, DomainSubscriber, TypedEvent> {

	public TypedEvent() {
		this.occurred = new Date();
	}

	protected final Date occurred;
	
	public String toString(){
		return super.toString() + " " + occurred ;
	}
}
