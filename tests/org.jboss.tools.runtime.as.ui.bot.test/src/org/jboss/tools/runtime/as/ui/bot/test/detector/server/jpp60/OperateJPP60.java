package org.jboss.tools.runtime.as.ui.bot.test.detector.server.jpp60;

import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

public class OperateJPP60 extends OperateServerTemplate {

	@Override
	protected String getServerName() {
		return DetectJPP60.SERVER_ID;
	}
}
