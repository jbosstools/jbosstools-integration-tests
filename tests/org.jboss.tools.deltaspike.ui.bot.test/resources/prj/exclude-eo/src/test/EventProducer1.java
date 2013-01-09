package test;

import javax.enterprise.event.Event;
import javax.inject.Inject;

public class EventProducer1 {

	@Inject
	private Event<PaymentEvent> event;
	
}
