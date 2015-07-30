package org.jboss.tools.runtime.as.ui.bot.test.detector.server.epp5;

import org.jboss.reddeer.requirements.jre.JRERequirement.JRE;
import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

@JRE(value=1.6, cleanup=true)
public class OperateEPP5 extends OperateServerTemplate {

	@Override
	protected String getServerName() {
		return DetectEPP5.SERVER_NAME;
	}
	
	@Override
	protected void assertNoError(String message) {
		//Skipped, because there is NoClassDefFoundError: org/jboss/cache/pojo/jmx/PojoCacheJmxWrapperMBean
	}
}
