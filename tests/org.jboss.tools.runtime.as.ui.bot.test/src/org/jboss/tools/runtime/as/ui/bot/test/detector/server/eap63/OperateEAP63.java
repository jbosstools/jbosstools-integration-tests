package org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap63;

import org.jboss.reddeer.requirements.jre.JRERequirement.JRE;
import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

@JRE(cleanup=true, value=1.7)
public class OperateEAP63 extends OperateServerTemplate {

	@Override
	protected String getServerName() {
		return DetectEAP63.SERVER_NAME;
	}
}
