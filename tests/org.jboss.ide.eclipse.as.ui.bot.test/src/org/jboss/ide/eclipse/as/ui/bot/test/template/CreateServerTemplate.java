package org.jboss.ide.eclipse.as.ui.bot.test.template;

import java.util.List;

import org.jboss.ide.eclipse.as.reddeer.server.editor.JBossServerEditor;
import org.jboss.ide.eclipse.as.reddeer.server.requirement.ServerRequirement;
import org.jboss.ide.eclipse.as.reddeer.server.view.JBossServer;
import org.jboss.ide.eclipse.as.reddeer.server.view.JBossServerView;
import org.jboss.ide.eclipse.as.reddeer.server.view.XMLConfiguration;
import org.jboss.reddeer.junit.requirement.inject.InjectRequirement;
import org.junit.Test;

/**
 * Creates server and checks its ports (in both the server view and server editor)
 * 
 * @author Lucia Jelinkova
 *
 */
public abstract class CreateServerTemplate {

	@InjectRequirement
	protected ServerRequirement requirement;
	
	protected JBossServerEditor editor;
	
	protected abstract void assertEditorPorts();
	
	protected abstract void assertViewPorts(List<XMLConfiguration> configurations);
	
	@Test
	public void createServer(){
		JBossServerView jbossView = new JBossServerView();
		JBossServer server = jbossView.getServer(getServerName());
		editor = server.open();

		assertEditorPorts();
		
		List<XMLConfiguration> configurations = server.getXMLConfiguration("Ports");
		assertViewPorts(configurations);
	}

	protected String getServerName() {
		return requirement.getServerNameLabelText(requirement.getConfig());
	}
}
