package org.jboss.tools.runtime.as.ui.bot.test.detector.server.jpp61x;

import org.jboss.reddeer.requirements.jre.JRERequirement.JRE;
import org.jboss.tools.runtime.as.ui.bot.test.template.OperateServerTemplate;

@JRE(value=1.7, cleanup=true)
public class OperateJPP61x extends OperateServerTemplate {

	@Override
	protected String getServerName() {
		return DetectJPP61x.SERVER_NAME;
	}
}
