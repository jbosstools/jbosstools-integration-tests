package org.jboss.tools.runtime.as.ui.bot.test.detector.server.soap53.standalone;

import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

public class OperateSOAPStandalone53 extends OperateServerTemplate {

	@Override
	protected String getServerName() {
		return DetectSOAPStandalone53.SERVER_NAME;
	}
}
