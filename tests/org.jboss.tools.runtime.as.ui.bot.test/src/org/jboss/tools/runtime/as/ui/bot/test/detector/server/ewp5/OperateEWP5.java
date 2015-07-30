package org.jboss.tools.runtime.as.ui.bot.test.detector.server.ewp5;

import org.jboss.reddeer.requirements.jre.JRERequirement.JRE;
import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

@JRE(value=1.6, cleanup=true)
public class OperateEWP5 extends OperateServerTemplate {

	@Override
	protected String getServerName() {
		return DetectEWP5.SERVER_NAME;
	}
}
