package org.jboss.ide.eclipse.as.ui.bot.test.template;

import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.view.ServersView;
import org.junit.Test;

/**
 * Deletes the server and checks that it is not present on the server's view. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class DeleteServer extends SWTTestExt {

	private ServersView serversView = new ServersView();
	
	@Test
	public void deleteServer(){
		serversView.deleteServer(configuredState.getServer().name);

		assertFalse(serversView.serverExists(configuredState.getServer().name));
	}
}
