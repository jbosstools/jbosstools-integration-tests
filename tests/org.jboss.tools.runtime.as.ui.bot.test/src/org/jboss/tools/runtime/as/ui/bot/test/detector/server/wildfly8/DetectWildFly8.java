package org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly8;

import java.util.Arrays;
import java.util.List;

import org.jboss.tools.runtime.as.ui.bot.test.RuntimeProperties;
import org.jboss.tools.runtime.as.ui.bot.test.entity.Runtime;
import org.jboss.tools.runtime.as.ui.bot.test.template.DetectRuntimeTemplate;

public class DetectWildFly8 extends DetectRuntimeTemplate {

	public static final String SERVER_ID = "wildfly-8.0";
	
	public static final String SERVER_NAME = "wildfly-8.0.0.Final";
	
	@Override
	protected String getPathID() {
		return SERVER_ID;
	}
	
	@Override
	protected List<Runtime> getExpectedRuntimes() {
		Runtime expectedServer = new Runtime();
		expectedServer.setName(SERVER_NAME);
		expectedServer.setVersion("8.0");
		expectedServer.setType("Wildfly");
		expectedServer.setLocation(RuntimeProperties.getInstance().getRuntimePath(SERVER_ID));
		return Arrays.asList(expectedServer);
	}
}
