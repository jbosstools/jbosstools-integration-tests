package org.jboss.tools.runtime.as.ui.bot.test.detector.server.fsw60;

import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

public class OperateFSW60 extends OperateServerTemplate {

	@Override
	protected String getServerName() {
		return DetectFSW60.SERVER_NAME;
	}
}
