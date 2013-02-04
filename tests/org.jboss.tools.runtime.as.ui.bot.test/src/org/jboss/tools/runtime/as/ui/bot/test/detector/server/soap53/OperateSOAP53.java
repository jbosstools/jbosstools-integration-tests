package org.jboss.tools.runtime.as.ui.bot.test.detector.server.soap53;

import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

public class OperateSOAP53 extends OperateServerTemplate {

	@Override
	protected String getServerName() {
		return DetectSOAP53.SERVER_NAME;
	}
}
