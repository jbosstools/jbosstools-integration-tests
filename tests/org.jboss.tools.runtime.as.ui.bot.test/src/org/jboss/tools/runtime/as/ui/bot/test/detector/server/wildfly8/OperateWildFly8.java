package org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly8;

import org.jboss.reddeer.requirements.jre.JRERequirement.JRE;
import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

@JRE(value=1.7, cleanup=true)
public class OperateWildFly8 extends OperateServerTemplate {

	@Override
	protected String getServerName() {
		return DetectWildFly8.SERVER_NAME;
	}
}
