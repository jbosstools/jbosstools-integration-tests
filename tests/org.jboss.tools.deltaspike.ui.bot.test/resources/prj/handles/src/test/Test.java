package test;

import org.apache.deltaspike.core.api.exception.control.annotation.ExceptionHandler;
import org.apache.deltaspike.core.api.exception.control.annotation.Handles;

@ExceptionHandler
public class Test {

	void printExceptions(@Handles String evt) {
		
	}
	
}
