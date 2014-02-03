package org.jboss.ide.eclipse.as.ui.bot.test.template;

import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.view.ServersView;
import org.junit.Test;

/**
 * Deletes the server and checks that it is not present on the server's view. 
 * 
 * NOTE: It is marked as abstract so that concrete implementation can specify their own {@link Server}
 * annotation
 * 
 * @author Lucia Jelinkova
 *
 */
public abstract class DeleteServerTemplate extends SWTTestExt {

	private ServersView serversView = new ServersView();
	
	@Test
	public void deleteServer(){
		serversView.deleteServer(getServerName());

		assertFalse(serversView.serverExists(configuredState.getServer().name));
	}
	
	protected String getServerName(){
		return configuredState.getServer().name;
	}
}
