package org.jboss.tools.openshift.ui.bot.test.explorer;

import org.jboss.tools.openshift.ui.bot.util.TestProperties;
import org.junit.Before;

public class ConnectionProd extends Connection {

	@Before
	public void setUpServer() {
		TestProperties.put("openshift.server.url", TestProperties
				.get("openshift.server.prod"));
	}
	
	
}
