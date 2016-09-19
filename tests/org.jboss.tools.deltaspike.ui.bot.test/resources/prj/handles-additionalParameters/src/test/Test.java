package test;

import org.apache.deltaspike.core.api.exception.control.ExceptionHandler;
import org.apache.deltaspike.core.api.exception.control.Handles;
import org.apache.deltaspike.core.api.exception.control.event.ExceptionEvent;

@ExceptionHandler
public class Test {

	//Any additional parameters of a handler method should be treated as injection points.
	void printExceptions(@Handles ExceptionEvent<Throwable> evt, String injectMe) {
		
	}
	
}
