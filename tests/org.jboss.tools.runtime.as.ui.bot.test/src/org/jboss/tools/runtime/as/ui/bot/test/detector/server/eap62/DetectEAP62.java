package org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap62;

import java.util.Arrays;
import java.util.List;

import org.jboss.reddeer.requirements.jre.JRERequirement.JRE;
import org.jboss.tools.runtime.as.ui.bot.test.RuntimeProperties;
import org.jboss.tools.runtime.as.ui.bot.test.entity.Runtime;
import org.jboss.tools.runtime.as.ui.bot.test.template.DetectRuntimeTemplate;

@JRE(cleanup=true, value=1.7)
public class DetectEAP62 extends DetectRuntimeTemplate {

	public static final String SERVER_ID = "jboss-eap-6.2";
	
	public static final String SERVER_NAME = "Red Hat JBoss EAP 6.2";
	
	@Override
	protected String getPathID() {
		return SERVER_ID;
	}

	@Override
	protected List<Runtime> getExpectedRuntimes() {
		Runtime server = new Runtime();
		server.setName(SERVER_NAME);
		server.setType("EAP");
		server.setVersion("6.2");
		server.setLocation(RuntimeProperties.getInstance().getRuntimePath(getPathID()));
		return Arrays.asList(server);
	}
}
