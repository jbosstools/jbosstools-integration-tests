package org.jboss.tools.ws.reddeer.jaxrs.core;

/**
 * Enumerates basic jax-rs annotations. 
 *
 * @author jjankovi
 * @author Radoslav Rabara
 *
 */
public enum RestFullAnnotations {

	GET("GET"),
	POST("POST"),
	PUT("PUT"),
	DELETE("DELETE");

	private String label; 

	private RestFullAnnotations(String label) {
		this.label = label;
	}

	/**
	 * Returns label.
	 */
	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return label;
	}
}
