package test;

import javax.enterprise.event.Event;
import javax.inject.Inject;

public class EventProducer2 {

	@Inject
	private Event<PaymentEvent> event;
	
}
