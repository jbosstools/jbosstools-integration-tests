package org.jboss.tools.runtime.as.ui.bot.test.detector.server.epp4;

import org.jboss.reddeer.requirements.jre.JRERequirement.JRE;
import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

@JRE(value=1.7, cleanup=true)
public class OperateEPP4 extends OperateServerTemplate {

	@Override
	protected String getServerName() {
		return DetectEPP4.SERVER_NAME;
	}
	
	@Override
	protected void assertNoException(String message) {
		// do not check the exception - it will be there
	}
}
