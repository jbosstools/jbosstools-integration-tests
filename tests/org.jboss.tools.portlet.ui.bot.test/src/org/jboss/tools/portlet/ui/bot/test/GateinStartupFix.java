package org.jboss.tools.portlet.ui.bot.test;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.portlet.ui.bot.test.testcase.SWTTaskBasedTestCase;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Seam;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.view.ServersView;
import org.junit.Test;

@Require(server=@Server(version="5.1.0", state=ServerState.Present), seam=@Seam)
public class GateinStartupFix extends SWTTaskBasedTestCase {

	@Test
	public void testFix(){
		ServersView serversView = SWTBotFactory.getServers();
		serversView.show();
		
		SWTBotTreeItem server = serversView.findServerByName(bot.tree(), configuredState.getServer().name);
		server.doubleClick();
		
		bot.hyperlink("Open launch configuration").click();

		SWTBotText vmArgumetsText = bot.textInGroup("VM arguments:"); 
		String vmArgs = vmArgumetsText.getText();
		vmArgumetsText.setText(vmArgs + " -Dexo.conf.dir.name=gatein ");
		
		bot.button("OK").click();
		System.out.println();
	}
}
