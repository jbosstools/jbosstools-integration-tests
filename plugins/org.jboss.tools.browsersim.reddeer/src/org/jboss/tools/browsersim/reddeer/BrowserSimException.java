package org.jboss.tools.browsersim.reddeer;

public class BrowserSimException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public BrowserSimException(String message) {
		super(message);
	}

	
	public BrowserSimException(String message, Throwable cause) {
		super(message, cause);
	}

}
