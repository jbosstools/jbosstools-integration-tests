package org.jboss.tools.ws.ui.bot.test.webservice;

/**
 * Web Service Runtime
 * Supported are JBossWS and ApacheCXF2.x
 * 
 * @author rrabara
 * 
 */
public enum WebServiceRuntime {
	JBOSS_WS	("JBossWS"),
	APACHE_CXF2	("Apache CXF 2.x");
	
	private final String name;
	
	private WebServiceRuntime(String wsRuntimeName) {
		this.name = wsRuntimeName;
	}
	
	public String getName() {
		return name;
	}
}
