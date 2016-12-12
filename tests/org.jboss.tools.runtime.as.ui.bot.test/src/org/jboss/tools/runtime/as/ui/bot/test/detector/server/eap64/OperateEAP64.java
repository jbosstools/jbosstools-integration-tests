package org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap64;

import org.jboss.reddeer.requirements.jre.JRERequirement.JRE;
import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

@JRE(cleanup=true, value=1.8)
public class OperateEAP64 extends OperateServerTemplate {

	@Override
	protected String getServerName() {
		return DetectEAP64.SERVER_NAME;
	}

}
