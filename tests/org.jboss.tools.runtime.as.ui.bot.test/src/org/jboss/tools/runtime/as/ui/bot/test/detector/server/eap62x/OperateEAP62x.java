package org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap62x;

import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

public class OperateEAP62x extends OperateServerTemplate {

	@Override
	protected String getServerName() {
		return DetectEAP62x.SERVER_NAME;
	}
}
