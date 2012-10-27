package domain.model.environment;

import domain.shared.ValueObject;

public class Host implements ValueObject {
	public Host(){}
	public Host(String name){
		this.name = name;
	}
	public String name;
	public String ip;
	
	public boolean equals(Object o){
		if(o instanceof Host){
			Host other = (Host) o;
			return other.name.equals(name);
		}
		return false;
	}
}
