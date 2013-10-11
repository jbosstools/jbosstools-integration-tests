package org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap60x;

import java.util.Arrays;
import java.util.List;

import org.jboss.tools.runtime.as.ui.bot.test.RuntimeProperties;
import org.jboss.tools.runtime.as.ui.bot.test.entity.Runtime;
import org.jboss.tools.runtime.as.ui.bot.test.template.DetectRuntimeTemplate;

/**
 * Detect EAP-6.0.x
 * 
 * @author rrabara
 *
 */
public class DetectEAP60x extends DetectRuntimeTemplate {

	public static final String SERVER_ID = "jboss-eap-6.0.x";
	
	public static final String SERVER_NAME= "jboss-eap-6.0.1";
	
	@Override
	protected String getPathID() {
		return SERVER_ID;
	}

	@Override
	protected List<Runtime> getExpectedRuntimes() {
		Runtime server = new Runtime();
		server.setName(SERVER_NAME);
		server.setType("EAP");
		server.setVersion("6.0");
		server.setLocation(RuntimeProperties.getInstance().getRuntimePath(getPathID()));
		return Arrays.asList(server);
	}
}
