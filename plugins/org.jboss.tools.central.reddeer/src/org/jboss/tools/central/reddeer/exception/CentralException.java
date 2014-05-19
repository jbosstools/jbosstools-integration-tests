package org.jboss.tools.central.reddeer.exception;

public class CentralException extends RuntimeException {
	
	public CentralException(String message) {
		super(message);
	}
	
	public CentralException(String message, Throwable cause){
		super(message, cause);
	}
	
	public CentralException(Throwable cause){
		super(cause);
	}

}
