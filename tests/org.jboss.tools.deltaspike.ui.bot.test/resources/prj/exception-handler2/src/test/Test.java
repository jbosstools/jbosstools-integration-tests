package test;

import org.apache.deltaspike.core.api.exception.control.BeforeHandles;
import org.apache.deltaspike.core.api.exception.control.event.ExceptionEvent;


public class Test {

	void beforePrint(@BeforeHandles ExceptionEvent<Throwable> evt) {
		
	}
	
}
