package org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap61x;

import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

/**
 * Operate EAP-6.1.x
 * 
 * @author rrabara
 *
 */
public class OperateEAP61x extends OperateServerTemplate {

	@Override
	protected String getServerName() {
		return DetectEAP61x.SERVER_NAME;
	}
}
