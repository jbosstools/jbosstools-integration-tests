package org.jboss.ide.eclipse.as.ui.bot.test.eap60;

import org.jboss.ide.eclipse.as.ui.bot.test.template.HotDeployJSPFileTemplate;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

@Require(server=@Server(type=ServerType.EAP, version="6", state=ServerState.Running), clearWorkspace=false, clearProjects=false)
public class HotDeployJSPFileEAP60Server extends HotDeployJSPFileTemplate {

}
