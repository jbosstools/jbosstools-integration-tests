package test;

import org.apache.deltaspike.core.api.exception.control.ExceptionHandler;
import org.apache.deltaspike.core.api.exception.control.BeforeHandles;

@ExceptionHandler
public class Test {

	void printExceptions(@BeforeHandles String evt) {
		
	}
	
}
