package org.jboss.ide.eclipse.as.ui.bot.test.as51;

import org.jboss.ide.eclipse.as.ui.bot.test.template.DeployJSPProjectTemplate;
import org.jboss.ide.eclipse.as.ui.bot.test.template.UndeployJSPProjectTemplate;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

/**
 * @see UndeployJSPProjectTemplate
 * @author Lucia Jelinkova
 *
 */
@Require(server=@Server(type=ServerType.JbossAS, version="5.1", state=ServerState.Running), clearProjects=false, clearWorkspace=false)
public class UndeployJSPProjectAS51Server extends UndeployJSPProjectTemplate {

	@Override
	protected String getConsoleMessage() {
		return "undeploy, ctxPath=/" + DeployJSPProjectTemplate.PROJECT_NAME;
	}
}
