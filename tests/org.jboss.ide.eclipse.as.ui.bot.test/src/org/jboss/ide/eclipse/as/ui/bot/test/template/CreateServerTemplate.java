package org.jboss.ide.eclipse.as.ui.bot.test.template;

import java.util.List;

import org.jboss.ide.eclipse.as.reddeer.server.editor.JBossServerEditor;
import org.jboss.ide.eclipse.as.reddeer.server.view.JBossServer;
import org.jboss.ide.eclipse.as.reddeer.server.view.XMLConfiguration;
import org.junit.Test;

/**
 * Creates server and checks its ports (in both the server view and server editor)
 * 
 * @author Lucia Jelinkova
 *
 */
public abstract class CreateServerTemplate extends AbstractJBossServerTemplate {

	protected JBossServerEditor editor;
	
	protected abstract void assertEditorPorts();
	
	protected abstract void assertViewPorts(List<XMLConfiguration> configurations);
	
	@Test
	public void createServer(){
		JBossServer server = getServer();
		editor = server.open();

		assertEditorPorts();
		List<XMLConfiguration> configurations = server.getXMLConfiguration("Ports");
		assertViewPorts(configurations);
	}
}
