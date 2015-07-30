package org.jboss.tools.runtime.as.ui.bot.test.detector.server.soap52.standalone;

import org.jboss.reddeer.requirements.jre.JRERequirement.JRE;
import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

@JRE(value=1.6, cleanup=true)
public class OperateSOAPStandalone52 extends OperateServerTemplate {

	@Override
	protected String getServerName() {
		return DetectSOAPStandalone52.SERVER_NAME;
	}
}
