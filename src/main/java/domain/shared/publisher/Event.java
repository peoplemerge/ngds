package domain.shared.publisher;

 abstract class Event <S , O extends Subscriber<S, O, A>, A extends Event<S, O, A>> {

}
