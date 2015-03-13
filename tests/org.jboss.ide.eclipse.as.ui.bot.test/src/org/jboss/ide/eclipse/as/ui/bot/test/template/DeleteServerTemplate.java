package org.jboss.ide.eclipse.as.ui.bot.test.template;

import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.eclipse.wst.server.ui.view.Server;
import org.junit.Test;

/**
 * Deletes the server and checks that it is not present on the server's view. 
 * 
 * NOTE: It is marked as abstract so that concrete implementation can specify their own {@link SWerver}
 * annotation
 * 
 * @author Lucia Jelinkova
 *
 */
public abstract class DeleteServerTemplate extends AbstractJBossServerTemplate {

	@Test(expected=EclipseLayerException.class)
	public void deleteServer(){
		getServer().delete(true);
		getServer();
	}
}
