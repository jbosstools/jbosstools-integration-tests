package org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap62;

import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

public class OperateEAP62 extends OperateServerTemplate {

	@Override
	protected String getServerName() {
		return DetectEAP62.SERVER_ID;
	}
}
