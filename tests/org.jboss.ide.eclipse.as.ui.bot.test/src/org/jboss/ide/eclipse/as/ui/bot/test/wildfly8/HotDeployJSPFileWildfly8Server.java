package org.jboss.ide.eclipse.as.ui.bot.test.wildfly8;

import org.jboss.ide.eclipse.as.ui.bot.test.template.HotDeployJSPFileTemplate;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

@Require(server=@Server(type=ServerType.WildFly, version="8", state=ServerState.Running), clearWorkspace=false, clearProjects=false)
public class HotDeployJSPFileWildfly8Server extends HotDeployJSPFileTemplate {

}
