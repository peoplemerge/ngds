package domain.shared.publisher;


 interface Subscriber<S, O extends Subscriber<S, O, A>, A extends Event<S, O, A>> {
	public void handle(A a);
}