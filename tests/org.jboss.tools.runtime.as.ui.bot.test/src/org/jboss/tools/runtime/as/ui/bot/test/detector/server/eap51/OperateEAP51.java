package org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap51;

import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

public class OperateEAP51 extends OperateServerTemplate {

	@Override
	protected String getServerName() {
		return DetectEAP51.SERVER_ID;
	}
}
