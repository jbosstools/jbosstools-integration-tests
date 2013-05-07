package org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap60;

import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

public class OperateEAP60 extends OperateServerTemplate {

	@Override
	protected String getServerName() {
		return DetectEAP60.SERVER_ID;
	}
}
