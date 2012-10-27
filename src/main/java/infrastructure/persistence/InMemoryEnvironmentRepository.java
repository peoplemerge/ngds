package infrastructure.persistence;

import java.util.ArrayList;
import java.util.List;

import domain.model.environment.Environment;
import domain.model.environment.EnvironmentRepository;

public class InMemoryEnvironmentRepository implements EnvironmentRepository {

	private List<Environment> environments = new ArrayList<Environment>();
	
	public void save(Environment environment) {
		environments.add(environment);
	}
	
	public String toString(){
		return environments.toString();
	}
}
