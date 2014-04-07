package org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap63;

import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

public class OperateEAP63 extends OperateServerTemplate {

	@Override
	protected String getServerName() {
		return DetectEAP63.SERVER_ID;
	}
}
