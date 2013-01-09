package test;

import org.apache.deltaspike.core.api.exception.control.annotation.Handles;
import org.apache.deltaspike.core.api.exception.control.event.ExceptionEvent;


public class Test {

	void printExceptions(@Handles ExceptionEvent<Throwable> evt) {
		
	}
	
}
