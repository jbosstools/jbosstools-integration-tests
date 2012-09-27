package org.jboss.ide.eclipse.as.ui.bot.test.template;

import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
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
@Require(server=@Server)
public abstract class DeleteServerTemplate extends SWTTestExt {

	private ServersView serversView = new ServersView();
	
	@Test
	public void deleteServer(){
		serversView.deleteServer(configuredState.getServer().name);

		assertFalse(serversView.serverExists(configuredState.getServer().name));
	}
}
