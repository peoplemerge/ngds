package domain.model.environment;

import domain.shared.DomainEvent;

public class EnvironmentEvent extends DomainEvent<EnvironmentEvent> {
	public final Environment environment;

	public final Type type;
	private Host host;
	private String command;

	private EnvironmentEvent(Type type, Environment environment) {
		this.environment = environment;
		this.type = type;
	}

	public String getCommand() {
		return command;
	}

	public Host getHost() {
		return host;
	}

	public static class Builder {
		private EnvironmentEvent event;

		public Builder(Type type, Environment environment) {
			event = new EnvironmentEvent(type, environment);
		}

		public Builder withHost(Host host) {
			event.host = host;
			return this;
		}

		public Builder withCommand(String command) {
			event.command = command;
			return this;
		}

		public EnvironmentEvent build() {
			return event;
		}
	}

	public boolean sameEventAs(EnvironmentEvent event) {
		if (event.environment.equals(environment) && event.type == type) {
			if (event.host != null && host != null) {
				return event.host.equals(host);
			}
			return true;
		}
		return false;
	}

	public String toString() {
		String hostStr = (host == null) ? "" : " host " + host.name;
		String commandStr = (command == null) ? "" : " command " + command;
		return this.getClass().getSimpleName() + " " + type + hostStr
				+ commandStr + " " + environment + " " + occurred;
	}
}
