package org.jboss.tools.runtime.as.ui.bot.test.detector.server.soap53;

import org.jboss.reddeer.requirements.jre.JRERequirement.JRE;
import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

@JRE(value=1.7, cleanup=true)
public class OperateSOAP53 extends OperateServerTemplate {

	@Override
	protected String getServerName() {
		return DetectSOAP53.SERVER_NAME;
	}
}
