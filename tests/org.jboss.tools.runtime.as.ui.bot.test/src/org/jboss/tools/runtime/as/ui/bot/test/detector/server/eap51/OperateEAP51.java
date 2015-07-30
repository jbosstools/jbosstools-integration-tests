package org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap51;

import org.jboss.reddeer.requirements.jre.JRERequirement.JRE;
import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

@JRE(value=1.6, cleanup=true)
public class OperateEAP51 extends OperateServerTemplate {

	@Override
	protected String getServerName() {
		return DetectEAP51.SERVER_NAME;
	}
}
