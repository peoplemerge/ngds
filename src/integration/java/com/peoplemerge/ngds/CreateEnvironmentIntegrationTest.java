package com.peoplemerge.ngds;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.peoplemerge.ngds.Node.Type;

public class CreateEnvironmentIntegrationTest {

	@Test
	public void createTestcluster() throws Exception {

		CreateEnvironmentCommand command = new CreateEnvironmentCommand.Builder(
				"puppet1env", new ZookeeperEnvironmentRepository(
						new ZookeeperPersistence("ino:2181"))).withNodes(1,
				Type.SMALL, new Dom0("root", "kowalski", new NfsMount()))
				.withConfigurationManagement(
						new Puppet(new Node("puppetmaster1", "peoplemerge.com",
								"192.168.10.137"))).withDispatch(
						new JschDispatch("root")).build();
		ExitCode exit = command.execute();
		assertEquals(ExitCode.SUCCESS, exit);

	}

}
