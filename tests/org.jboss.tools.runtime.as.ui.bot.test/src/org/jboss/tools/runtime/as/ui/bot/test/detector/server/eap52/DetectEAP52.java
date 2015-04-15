package org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap52;

import java.util.Arrays;
import java.util.List;

import org.jboss.tools.runtime.as.ui.bot.test.RuntimeProperties;
import org.jboss.tools.runtime.as.ui.bot.test.entity.Runtime;
import org.jboss.tools.runtime.as.ui.bot.test.template.DetectRuntimeTemplate;

public class DetectEAP52 extends DetectRuntimeTemplate {

	public static final String SERVER_ID = "jboss-eap-5.2";
	public static final String SERVER_NAME = "JBoss EAP 5.2";
	
	@Override
	protected String getPathID() {
		return SERVER_ID;
	}

	@Override
	protected List<Runtime> getExpectedRuntimes() {
		Runtime server = new Runtime();
		server.setName(SERVER_NAME);
		server.setType("EAP");
		server.setVersion("5.2");
		server.setLocation(RuntimeProperties.getInstance().getRuntimePath(getPathID()));
		
		Runtime seam = new Runtime();
		seam.setName("seam");
		seam.setType("SEAM");
		seam.setVersion("2.2.6.EAP5");
		seam.setLocation(RuntimeProperties.getInstance().getRuntimePath(getPathID()) + "/seam");
		
		return Arrays.asList(server, seam);
	}
}
