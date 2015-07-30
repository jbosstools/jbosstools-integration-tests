package org.jboss.tools.runtime.as.ui.bot.test.detector.server.jboss7;

import org.jboss.reddeer.requirements.jre.JRERequirement.JRE;
import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

@JRE(value=1.7, cleanup=true)
public class OperateJBoss7 extends OperateServerTemplate{

	@Override
	protected String getServerName() {
		return DetectJBoss7.SERVER_NAME;
	}
}
