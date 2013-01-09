package test;

import org.apache.deltaspike.core.api.exception.control.annotation.ExceptionHandler;
import org.apache.deltaspike.core.api.exception.control.annotation.BeforeHandles;

@ExceptionHandler
public class Test {

	void printExceptions(@BeforeHandles String evt) {
		
	}
	
}
