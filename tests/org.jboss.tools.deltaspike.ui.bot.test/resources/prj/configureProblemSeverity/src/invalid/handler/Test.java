package invalid.handler;

import org.apache.deltaspike.core.api.exception.control.ExceptionHandler;
import org.apache.deltaspike.core.api.exception.control.Handles;

@ExceptionHandler
public class Test {

	void printExceptions(@Handles String evt) {
		
	}
	
}
