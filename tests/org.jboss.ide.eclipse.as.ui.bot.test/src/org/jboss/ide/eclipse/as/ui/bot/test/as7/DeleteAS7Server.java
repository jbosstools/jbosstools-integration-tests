package org.jboss.ide.eclipse.as.ui.bot.test.as7;

import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.view.ServersView;
import org.junit.Test;

/**
 * Deletes the server. 
 * 
 * @author Lucia Jelinkova
 *
 */
public class DeleteAS7Server extends SWTTestExt {

	private ServersView serversView = new ServersView();
	
	@Test
	public void deleteServer(){
		serversView.deleteServer(configuredState.getServer().name);

		assertFalse(serversView.serverExists(configuredState.getServer().name));
	}
}
