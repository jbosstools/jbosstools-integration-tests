package org.jboss.tools.ws.ui.bot.test.rest;

public enum RESTFulAnnotations {
	
	CONFIGURE_MENU_LABEL("Configure"),
	REST_SUPPORT_MENU_LABEL_ADD("Add JAX-RS 1.1 support..."),
	REST_SUPPORT_MENU_LABEL_REMOVE("Remove JAX-RS 1.1 support..."),
	REST_EXPLORER_LABEL("JAX-RS REST Web Services"),
	REST_EXPLORER_LABEL_BUILD("Building RESTful Web Services..."),
	GET("GET"),
	POST("POST"),
	PUT("PUT"),
	DELETE("DELETE");
	
	private String label; 
	
	private RESTFulAnnotations(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}

}
