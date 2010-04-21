package org.jboss.tools.ui.bot.ext.config.requirement;

import org.jboss.tools.ui.bot.ext.SWTTestExt;
/**
 * Starts server (as dependent requirement has {@link AddServer}
 * @author lzoubek
 *
 */
public class StartServer extends RequirementBase {

	public StartServer() {
		// define dependency
		getDependsOn().add(new AddServer());
	}
	
	@Override
	public boolean checkFullfilled() {
		return SWTTestExt.configuredState.getServer().isRunning;
	}

	@Override
	public void handle(){
		SWTTestExt.servers.startServer(SWTTestExt.configuredState.getServer().name);
		SWTTestExt.configuredState.getServer().isRunning = true;

	}
}
