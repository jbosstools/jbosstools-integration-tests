package org.jboss.tools.runtime.as.ui.bot.test.detector.server.wildfly81;

import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

public class OperateWildFly81 extends OperateServerTemplate {

	@Override
	protected String getServerName() {
		return DetectWildFly81.SERVER_NAME;
	}
}
