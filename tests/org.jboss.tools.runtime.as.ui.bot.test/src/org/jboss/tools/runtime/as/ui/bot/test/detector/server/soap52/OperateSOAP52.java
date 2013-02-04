package org.jboss.tools.runtime.as.ui.bot.test.detector.server.soap52;

import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

public class OperateSOAP52 extends OperateServerTemplate {

	@Override
	protected String getServerName() {
		return DetectSOAP52.SERVER_NAME;
	}
}
