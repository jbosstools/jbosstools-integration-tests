package org.jboss.tools.runtime.as.ui.bot.test.detector.server.eap60x;

import org.jboss.reddeer.requirements.jre.JRERequirement.JRE;
import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

/**
 * Operate EAP-6.0.x
 * 
 * @author rrabara
 *
 */

@JRE(value=1.7, cleanup=true)
public class OperateEAP60x extends OperateServerTemplate {

	@Override
	protected String getServerName() {
		return DetectEAP60x.SERVER_NAME;
	}
}
