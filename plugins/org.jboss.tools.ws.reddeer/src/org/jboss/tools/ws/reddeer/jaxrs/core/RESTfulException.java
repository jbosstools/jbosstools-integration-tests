package org.jboss.tools.ws.reddeer.jaxrs.core;

public class RESTfulException extends RuntimeException {

	private static final long serialVersionUID = 5027093147981702955L;

	public RESTfulException(String message) {
		super(message);
	}
	
	public RESTfulException(String message, Throwable cause) {
		super(message, cause);
	}
}
