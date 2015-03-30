package org.jboss.tools.runtime.as.ui.bot.test.detector.server.fsw60;

import java.util.Arrays;
import java.util.List;

import org.jboss.tools.runtime.as.ui.bot.test.RuntimeProperties;
import org.jboss.tools.runtime.as.ui.bot.test.entity.Runtime;
import org.jboss.tools.runtime.as.ui.bot.test.template.DetectRuntimeTemplate;

/**
 * 
 * @author Radoslav Rabara
 * @see https://issues.jboss.org/browse/JBIDE-17033
 */
public class DetectFSW60 extends DetectRuntimeTemplate {

	public static final String SERVER_ID = "jboss-fsw-6.0";
	public static final String SERVER_NAME = "JBoss Fuse Service Works 6.0";
	
	@Override
	protected String getPathID() {
		return SERVER_ID;
	}

	@Override
	protected List<Runtime> getExpectedRuntimes() {
		Runtime server = new Runtime();
		server.setName(SERVER_NAME);
		server.setType("FSW");
		server.setVersion("6.0");
		server.setLocation(RuntimeProperties.getInstance().getRuntimePath(getPathID()) + "/jboss-eap-6.1");
		return Arrays.asList(server);
	}
}
