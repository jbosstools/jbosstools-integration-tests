package org.jboss.tools.runtime.as.ui.bot.test.detector.server.jpp60;

import org.jboss.reddeer.requirements.jre.JRERequirement.JRE;
import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

@JRE(value=1.7, cleanup=true)
public class OperateJPP60 extends OperateServerTemplate {

	@Override
	protected String getServerName() {
		return DetectJPP60.SERVER_NAME;
	}
}
