package org.jboss.ide.eclipse.as.ui.bot.test.template;

import java.util.List;

import org.jboss.ide.eclipse.as.reddeer.server.editor.JBossServerEditor;
import org.jboss.ide.eclipse.as.reddeer.server.view.JBossServer;
import org.jboss.ide.eclipse.as.reddeer.server.view.XMLConfiguration;
import org.jboss.reddeer.common.logging.Logger;
import org.junit.Test;

/**
 * Creates server and checks its ports (in both the server view and server editor)
 * 
 * @author Lucia Jelinkova
 *
 */
public abstract class CreateServerTemplate extends AbstractJBossServerTemplate {

	private static final Logger log = Logger.getLogger(CreateServerTemplate.class);
	
	protected JBossServerEditor editor;
	
	protected abstract void assertEditorPorts();
	
	protected abstract void assertViewPorts(List<XMLConfiguration> configurations);
	
	@Test
	public void createServer(){
		JBossServer server = getServer();
		
		log.step("Open server editor");
		editor = server.open();

		log.step("Assert editor ports in the editor");
		assertEditorPorts();
		
		log.step("Retrieve XML configuration");
		List<XMLConfiguration> configurations = server.getXMLConfiguration("Ports");
		
		log.step("Assert XML configuration");
		assertViewPorts(configurations);
	}
}
