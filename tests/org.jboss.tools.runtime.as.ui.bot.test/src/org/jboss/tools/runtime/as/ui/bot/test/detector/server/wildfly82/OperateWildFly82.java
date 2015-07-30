package org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly82;

import org.jboss.reddeer.requirements.jre.JRERequirement.JRE;
import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

@JRE(cleanup=true)
public class OperateWildFly82 extends OperateServerTemplate {
	
	@Override
	protected String getServerName() {
		return DetectWildFly82.SERVER_NAME;
	}
	
}
