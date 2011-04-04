package org.jboss.tools.bpel.ui.bot.test;

import org.eclipse.core.resources.IFile;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.bpel.ui.bot.test.suite.BPELTest;
import org.jboss.tools.bpel.ui.bot.test.util.ResourceHelper;
import org.jboss.tools.ui.bot.ext.config.Annotations.SWTBotTestRequires;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.view.ProjectExplorer;
import org.jboss.tools.ui.bot.ext.view.ServersView;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * 
 * @author psrna
 *
 */
@SWTBotTestRequires(server = @Server(type = ServerType.SOA, state = ServerState.Running), perspective="BPEL Perspective")
public class OdeDeployTest extends BPELTest {
	
	static String BUNDLE   = "org.jboss.tools.bpel.ui.bot.test";
	ServersView sView = new ServersView();
	ProjectExplorer projExplorer = new ProjectExplorer(){
		
		@Override
		public void runOnServer(String projectName) {
			String serverName = AssignActivityTest.configuredState.getServer().name;
			//serverName = "SOA-5.1";

			bot.viewByTitle("Servers").show();
			bot.viewByTitle("Servers").setFocus();
			
			SWTBotTree tree = bot.viewByTitle("Servers").bot().tree(); 
			SWTBotTreeItem server = tree.getTreeItem(serverName + "  [Started, Synchronized]").select();
			
			ContextMenuHelper.prepareTreeItemForContextMenu(tree, server);
			new SWTBotMenu(ContextMenuHelper.getContextMenu(tree, IDELabel.Menu.ADD_AND_REMOVE, false)).click();
			
			SWTBotShell shell = OdeDeployTest.bot.shell("Add and Remove...");
			shell.activate();
			
			SWTBot viewBot = shell.bot();
			viewBot.tree().setFocus();
			viewBot.tree().select(projectName);
			viewBot.button("Add >").click();
			viewBot.button("Finish").click();
		}
	};
	
	@BeforeClass
	public static void setupWorkspace() throws Exception {
		ResourceHelper.importProject(BUNDLE, "/projects/bpel_say_hello", "say_hello");
		bot.viewByTitle("Project Explorer").setFocus();
	}
	
	@Test
	public void deploymentDescriptorTest() throws Exception {
		
		IFile deployFile = createNewDeployDescriptor("say_hello");

		bot.editorByTitle("deploy.xml").show();
		bot.editorByTitle("deploy.xml").setFocus();
		SWTBot editorBot = bot.editorByTitle("deploy.xml").bot();
		
		SWTBotTable table = editorBot.table(0);
		table.click(0, 1);
		bot.sleep(TIME_1S);
		
		editorBot.ccomboBox("-- none -- ").setSelection("SayHelloPort");
		table.click(0, 2);
		bot.editorByTitle("deploy.xml").save();
		
		String deployContent = loadFile(deployFile);
		Assert.assertTrue(deployContent != null);
		Assert.assertTrue(deployContent.contains("<deploy xmlns=\"http://www.apache.org/ode/schemas/dd/2007/03\" xmlns:examples=\"http://www.jboss.org/bpel/examples\">"));
		Assert.assertTrue(deployContent.contains("<process name=\"examples:HelloWorld\">"));
		Assert.assertTrue(deployContent.contains("<active>true</active>"));
		Assert.assertTrue(deployContent.contains("<retired>false</retired>"));
		Assert.assertTrue(deployContent.contains("<process-events generate=\"all\"/>"));
		Assert.assertTrue(deployContent.contains("<provide partnerLink=\"client\">"));
		Assert.assertTrue(deployContent.contains("<service name=\"examples:SayHelloService\" port=\"SayHelloPort\"/>"));
		Assert.assertTrue(deployContent.contains("</provide>"));
		Assert.assertTrue(deployContent.contains("</process>"));
		Assert.assertTrue(deployContent.contains("</deploy>"));
			
		bot.sleep(TIME_10S);
		
	}
	
	@Test
	public void deployProjectTest() throws Exception {
		String serverName = OdeDeployTest.configuredState.getServer().name;
		// Publish the process
		projExplorer.runOnServer("say_hello");
		bot.sleep(TIME_5S);
		Assert.assertFalse(console.getConsoleText().contains("DEPLOYMENTS IN ERROR:"));
		
		bot.viewByTitle("Servers").show();
		bot.viewByTitle("Servers").setFocus();
		
		SWTBotTree tree = bot.viewByTitle("Servers").bot().tree(); 
		SWTBotTreeItem server = tree.getTreeItem(serverName + "  [Started, Synchronized]").select();
		server.expand();
		bot.sleep(TIME_5S);
		assertTrue(server.getNode("say_hello  [Synchronized]").isVisible());
		
	}
	
	
	
	
	
	
	

	
	
}
