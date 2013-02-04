package org.jboss.tools.runtime.as.ui.bot.test.detector.server.soap52.standalone;

import java.util.Arrays;
import java.util.List;

import org.jboss.tools.runtime.as.ui.bot.test.RuntimeProperties;
import org.jboss.tools.runtime.as.ui.bot.test.entity.Runtime;
import org.jboss.tools.runtime.as.ui.bot.test.template.DetectRuntimeTemplate;

public class DetectSOAPStandalone52 extends DetectRuntimeTemplate {

	public static final String SERVER_ID = "jboss-soa-p-standalone-5.2";
	public static final String SERVER_NAME = "jboss-soa-p-standalone-5";
	
	@Override
	protected String getPathID() {
		return SERVER_ID;
	}
	
	@Override
	protected List<Runtime> getExpectedRuntimes() {
		Runtime expectedServer = new Runtime();
		expectedServer.setName(SERVER_NAME);
		expectedServer.setVersion("5.2");
		expectedServer.setType("SOA-P-STD");
		expectedServer.setLocation(RuntimeProperties.getInstance().getRuntimePath(SERVER_ID));
		return Arrays.asList(expectedServer);
	}
}
