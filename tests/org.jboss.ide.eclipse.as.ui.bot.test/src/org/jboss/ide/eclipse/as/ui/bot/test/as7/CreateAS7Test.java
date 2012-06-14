package org.jboss.ide.eclipse.as.ui.bot.test.as7;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.jboss.ide.eclipse.as.ui.bot.test.editor.ServerEditor;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.entity.XMLConfiguration;
import org.jboss.tools.ui.bot.ext.view.ServersView;
import org.junit.Test;

/**
 * Creates server and checks its ports (in both the server view and server editor) 
 * 
 * @author Lucia Jelinkova
 *
 */
@Require(server=@Server(type=ServerType.EAP, state=ServerState.Present))
public class CreateAS7Test extends SWTTestExt {

	@Test
	public void createServer(){
		ServerEditor editor = new ServerEditor(configuredState.getServer().name);
		editor.open();
		
		assertThat("8080", is(editor.getWebPort()));
		assertThat("9999", is(editor.getManagementPort()));
		
		ServersView view = new ServersView();
		List<XMLConfiguration> configurations = view.getXMLConfiguration(configuredState.getServer().name, "Ports");
		assertThat(configurations, hasItem(new XMLConfiguration("JBoss Management", "${jboss.management.native.port:9999}")));
		assertThat(configurations, hasItem(new XMLConfiguration("JBoss Web", "8080")));
	}
}
