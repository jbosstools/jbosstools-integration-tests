package org.jboss.ide.eclipse.as.ui.bot.test.template;

import java.util.List;

import org.jboss.ide.eclipse.as.ui.bot.test.editor.ServerEditor;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.entity.XMLConfiguration;
import org.jboss.tools.ui.bot.ext.view.ServersView;
import org.junit.Test;

/**
 * Creates server and checks its ports (in both the server view and server editor)
 * 
 * @author Lucia Jelinkova
 *
 */
public abstract class CreateServerTemplate extends SWTTestExt {

	protected ServerEditor editor;
	
	protected abstract void assertEditorPorts();
	
	protected abstract void assertViewPorts(List<XMLConfiguration> configurations);
	
	@Test
	public void createServer(){
		new org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView().open();
		editor = new ServerEditor(getServerName());
		editor.open();
		
		assertEditorPorts();
		
		ServersView view = new ServersView();
		List<XMLConfiguration> configurations = view.getXMLConfiguration(getServerName(), "Ports");
		assertViewPorts(configurations);
	}
	
	protected String getServerName(){
		return configuredState.getServer().name;
	}
}
