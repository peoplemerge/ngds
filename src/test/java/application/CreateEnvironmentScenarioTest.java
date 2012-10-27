package application;

import infrastructure.persistence.InMemoryEnvironmentRepository;
import infrastructure.persistence.InMemoryEventStore;

import org.junit.Assert;
import org.junit.Test;

import domain.model.environment.EnvironmentRepository;
import domain.model.environment.EventStore;
import domain.model.execution.ExitCode;
import domain.model.execution.Saga;

public class CreateEnvironmentScenarioTest {

	@Test
	public void testScenario() {

		EventStore eventStore = new InMemoryEventStore();
		EnvironmentRepository repo = new InMemoryEnvironmentRepository();

		CreateEnvironmentCommand command = new CreateEnvironmentCommand.Builder()
				.withEventStore(eventStore)
				.withEnvironmentRepository(repo)
				.build();
		Saga saga = command.create();
		System.out.println("saga: " + saga);
		ExitCode exit = saga.execute();
		Assert.assertEquals(ExitCode.SUCCESS, exit);
		System.out.println("event store: " + eventStore);
		System.out.println("repo: " + repo);
	}
}
