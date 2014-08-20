package org.jboss.tools.ws.reddeer.jaxrs.core;

/**
 * Enumerates jax-rs labels. 
 *
 * @author jjankovi
 * @author Radoslav Rabara
 *
 */
public enum RestFullLabels {

	REST_SUPPORT_MENU_LABEL_ADD("Add JAX-RS Support..."),
	REST_SUPPORT_MENU_LABEL_REMOVE("Remove JAX-RS Support..."),
	REST_EXPLORER_LABEL("JAX-RS Web Services"),
	REST_EXPLORER_LABEL_BUILD("Building RESTful Web Services...");

	private String label; 

	private RestFullLabels(String label) {
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
