package org.jboss.ide.eclipse.as.ui.bot.test.template;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
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

	private static final Logger log = Logger.getLogger(DeleteServerTemplate.class);
	
	@Test(expected=EclipseLayerException.class)
	public void deleteServer(){
		log.step("Delete server");
		getServer().delete(true);
		log.step("Assert the server is not on the Servers view");
		getServer();
	}
}
