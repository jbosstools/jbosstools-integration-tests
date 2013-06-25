package org.jboss.tools.runtime.as.ui.bot.test.detector.server.jpp6;

import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

public class OperateJPP6 extends OperateServerTemplate {

	@Override
	protected String getServerName() {
		return DetectJPP6.SERVER_ID;
	}
}
