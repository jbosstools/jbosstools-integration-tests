package org.jboss.ide.eclipse.as.ui.bot.test.as40;

import org.jboss.ide.eclipse.as.ui.bot.test.template.OperateServerTemplate;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

/**
 * @see OperateServerTemplate
 * @author Lucia Jelinkova
 *
 */
@Require(server=@Server(type=ServerType.JbossAS, version="4.0", state=ServerState.NotRunning))
public class OperateAS40Server extends OperateServerTemplate {

	@Override
	public String getWelcomePageText() {
		return "JBoss Management";
	}
}
