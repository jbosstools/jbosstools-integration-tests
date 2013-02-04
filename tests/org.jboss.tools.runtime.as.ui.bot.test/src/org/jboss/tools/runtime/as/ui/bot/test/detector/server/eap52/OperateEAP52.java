package org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap52;

import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

public class OperateEAP52 extends OperateServerTemplate {

	@Override
	protected String getServerName() {
		return DetectEAP52.SERVER_ID;
	}
}
