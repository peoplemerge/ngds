package domain.shared.publisher;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class DomainPublisherTest {

	private class TestEvent extends Event<Publisher, TestSubscriber, TestEvent> {
	}

	private class TestSubscriber implements
			Subscriber<Publisher, TestSubscriber, TestEvent> {
		public boolean handled = false;

		public void handle(TestEvent a) {
			handled = true;
		}

	}

	@Test
	public void addSubscriber() {
		Publisher<Publisher, TestSubscriber, TestEvent> publisher = new Publisher<Publisher, TestSubscriber, TestEvent>();

		// Add a subscriber
		TestSubscriber subscriber = new TestSubscriber();
		Assert.assertTrue(subscriber.handled == false);
		publisher.addSubscriber(subscriber, new TestEvent());
		// Create an event
		TestEvent testEvent = new TestEvent();
		// fire the event
		publisher.publish(testEvent);
		Assert.assertTrue(subscriber.handled == true);
	}

	private class AnotherTestEvent extends
			Event<Publisher, AnotherTestSubscriber, AnotherTestEvent> {
	}

	private class AnotherTestSubscriber implements
			Subscriber<Publisher, AnotherTestSubscriber, AnotherTestEvent> {
		public boolean handled = false;

		public void handle(AnotherTestEvent a) {
			handled = true;
		}

	}

	@Test
	@Ignore
	// This test is preserved for reference purposes during development. Do no
	// use!
	public void eventsNonGeneric() {
		Publisher publisher = new Publisher();
		TestSubscriber subscriber = new TestSubscriber();
		publisher.addSubscriber(subscriber, new TestEvent());
		AnotherTestSubscriber anotherSubscriber = new AnotherTestSubscriber();
		publisher.addSubscriber(anotherSubscriber, new AnotherTestEvent());

		TestEvent testEvent = new TestEvent();
		// fire the first event
		publisher.publish(testEvent);
		Assert.assertTrue(subscriber.handled == true);
		Assert.assertTrue(anotherSubscriber.handled == false);
		AnotherTestEvent anotherEvent = new AnotherTestEvent();
		publisher.publish(anotherEvent);
		Assert.assertTrue(anotherSubscriber.handled == true);

	}

	private class GenericSubscriber implements DomainSubscriber {
		public boolean handled = false;

		public void handle(TypedEvent a) {
			handled = true;
		}
	}

	private class GenericEvent extends DomainEvent<GenericEvent> {
		public boolean sameEventAs(GenericEvent other) {
			return true;
		}
	}

	private class AnotherGenericSubscriber implements DomainSubscriber {
		public boolean handled = false;

		public void handle(TypedEvent a) {
			handled = true;
		}

	}

	private class AnotherGenericEvent extends DomainEvent<AnotherGenericEvent> {
		public boolean sameEventAs(AnotherGenericEvent other) {
			return true;
		}
	}

	@Test
	public void generic() {
		DomainEventPublisher publisher = new DomainEventPublisher();
		GenericSubscriber subscriber = new GenericSubscriber();
		publisher.addSubscriber(subscriber, new GenericEvent());
		AnotherGenericSubscriber anotherSubscriber = new AnotherGenericSubscriber();
		publisher.addSubscriber(anotherSubscriber, new AnotherGenericEvent());

		GenericEvent testEvent = new GenericEvent();
		// fire the first event
		publisher.publish(testEvent);
		Assert.assertTrue(subscriber.handled == true);
		Assert.assertTrue(anotherSubscriber.handled == false);
		AnotherGenericEvent anotherEvent = new AnotherGenericEvent();
		publisher.publish(anotherEvent);
		Assert.assertTrue(anotherSubscriber.handled == true);
	}

	@Test
	public void multipleSubscribersToEventType() {
		DomainEventPublisher publisher = new DomainEventPublisher();
		GenericSubscriber subscriber = new GenericSubscriber();
		publisher.addSubscriber(subscriber, new GenericEvent());
		AnotherGenericSubscriber anotherSubscriber = new AnotherGenericSubscriber();
		publisher.addSubscriber(anotherSubscriber, new GenericEvent());

		GenericEvent testEvent = new GenericEvent();
		// fire the first event
		publisher.publish(testEvent);
		Assert.assertTrue(subscriber.handled == true);
		Assert.assertTrue(anotherSubscriber.handled == true);
	}
	



}
