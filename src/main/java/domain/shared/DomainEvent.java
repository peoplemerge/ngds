package domain.shared;

import java.util.Date;

public abstract class DomainEvent {

	public DomainEvent() {
		this.occurred = new Date();
	}

	protected final Date occurred;
	
	public String toString(){
		return super.toString() + " " + occurred ;
	}
	
	public abstract boolean sameEventAs(DomainEvent event);
	
}
