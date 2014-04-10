package org.jboss.tools.runtime.as.ui.bot.test.detector.seam.seam23x;

import org.jboss.tools.runtime.as.ui.bot.test.RuntimeProperties;
import org.jboss.tools.runtime.as.ui.bot.test.entity.Runtime;
import org.jboss.tools.runtime.as.ui.bot.test.template.CheckSeamRuntimeTemplate;

public class CheckSeam23x extends CheckSeamRuntimeTemplate {

	@Override
	protected Runtime getExpectedRuntime() {
		Runtime server = new Runtime();
		server.setName("Seam " + DetectSeam23x.NAME + " 2.3");
		server.setVersion("2.3");
		server.setLocation(RuntimeProperties.getInstance().getRuntimePath(DetectSeam23x.PATH_ID));
		return server;
	}
}
