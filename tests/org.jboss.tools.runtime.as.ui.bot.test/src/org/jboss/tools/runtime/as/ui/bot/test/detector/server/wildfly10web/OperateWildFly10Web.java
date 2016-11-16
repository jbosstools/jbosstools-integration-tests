package org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly10web;

import org.jboss.reddeer.requirements.jre.JRERequirement.JRE;
import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

@JRE(cleanup=true, value=1.8)
public class OperateWildFly10Web extends OperateServerTemplate {
	
	@Override
	protected String getServerName() {
		return DetectWildFly10Web.SERVER_NAME;
	}
}
