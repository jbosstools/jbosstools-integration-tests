package org.jboss.tools.openshift.ui.bot.test.explorer;

import org.jboss.tools.openshift.ui.bot.util.TestProperties;
import org.junit.Before;

public class ConnectionEnterprise extends Connection{

	@Before
	public void setUpServer() {
		TestProperties.put("openshift.server.url", TestProperties
				.get("openshift.server.prod"));
		
		TestProperties.put("openshift.user.name", "demo");
		TestProperties.put("openshift.user.pwd", "changeme");
	}
	
}
