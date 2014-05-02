package org.jboss.ide.eclipse.as.ui.bot.test.template;

import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
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
public abstract class DeleteServerTemplate {

	@InjectRequirement
	protected ServerRequirement requirement;
	
	private ServersView serversView = new ServersView();
	
	@Test(expected=EclipseLayerException.class)
	public void deleteServer(){
		serversView.getServer(getServerName()).delete(true);
		serversView.getServer(getServerName());
	}

	protected String getServerName() {
		return requirement.getServerNameLabelText(requirement.getConfig());
	} 
}
