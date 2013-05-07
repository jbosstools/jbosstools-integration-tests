package org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap61;

import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

public class OperateEAP61 extends OperateServerTemplate {

	@Override
	protected String getServerName() {
		return DetectEAP61.SERVER_ID;
	}
}
