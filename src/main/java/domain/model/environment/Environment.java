package domain.model.environment;

import java.util.ArrayList;
import java.util.List;

import domain.shared.AggregateRoot;
import domain.shared.Entity;


public class Environment implements Entity, AggregateRoot{
	public List<Host> hosts = new ArrayList<Host>();
	public void add(Host host){
		hosts.add(host);
	}
	public boolean equals(Object o){
		if(o instanceof Environment){
			Environment other = (Environment) o;
			return hosts.equals(other.hosts);
		}
		return false;
	}
}
