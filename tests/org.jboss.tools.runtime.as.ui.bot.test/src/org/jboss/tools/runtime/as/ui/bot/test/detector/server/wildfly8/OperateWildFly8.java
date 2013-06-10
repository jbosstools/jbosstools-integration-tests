package org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly8;

import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

public class OperateWildFly8 extends OperateServerTemplate {

	@Override
	protected String getServerName() {
		return DetectWildFly8.SERVER_ID;
	}
}
