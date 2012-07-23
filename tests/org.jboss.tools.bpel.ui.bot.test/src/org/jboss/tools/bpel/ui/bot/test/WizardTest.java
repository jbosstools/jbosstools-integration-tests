package org.jboss.tools.bpel.ui.bot.test;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.utils.TableCollection;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTableItem;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.Assert;
import org.junit.Test;

@Require(server = @Server(type = ServerType.ALL, state = ServerState.Present), perspective="BPEL")
public class WizardTest extends BPELTest {

	
	@Test
	public void createNewSyncProcess() throws Exception {
		IProject project = createNewProject("SyncProcessProject");
		IFile process =    createNewProcess("SyncProcessProject", "SyncProcess", BPELConstants.SYNC_PROCESS_LABEL, false);
		
		process.getFileExtension();
		String processContent = loadFile(process);
		
		Assert.assertTrue(processContent != null);
		Assert.assertTrue(processContent.contains("http://docs.oasis-open.org/wsbpel/2.0/process/executable"));
		Assert.assertTrue(processContent.contains("<bpel:import location=\"SyncProcessArtifacts.wsdl\""));
		Assert.assertTrue(processContent.contains("<bpel:receive name=\"receiveInput\""));
		Assert.assertTrue(processContent.contains("<bpel:reply name=\"replyOutput\""));
		Assert.assertTrue(isRuntimeSet("SyncProcessProject"));
	}
	
	@Test
	public void createNewAsyncProcess() throws Exception {
		IProject project = createNewProject("AsyncProcessProject");
		IFile process =    createNewProcess("AsyncProcessProject", "AsyncProcess", BPELConstants.ASYNC_PROCESS_LABEL, false);
		
		process.getFileExtension();
		String processContent = loadFile(process);
		
		Assert.assertTrue(processContent != null);
		Assert.assertTrue(processContent.contains("http://docs.oasis-open.org/wsbpel/2.0/process/executable"));
		Assert.assertTrue(processContent.contains("<bpel:import location=\"AsyncProcessArtifacts.wsdl\""));
		Assert.assertTrue(processContent.contains("<bpel:receive name=\"receiveInput\""));
		Assert.assertTrue(processContent.contains("<bpel:invoke name=\"callbackClient\""));
		Assert.assertTrue(isRuntimeSet("AsyncProcessProject"));
	}
	
	@Test
	public void createNewEmptyProcess() throws Exception {
		IProject project = createNewProject("EmptyProcessProject");
		IFile process =    createNewProcess("EmptyProcessProject", "EmptyProcess", BPELConstants.EMPTY_PROCESS_LABEL, false);
		
		process.getFileExtension();
		String processContent = loadFile(process);
		
		Assert.assertTrue(processContent != null);
		Assert.assertTrue(processContent.contains("http://docs.oasis-open.org/wsbpel/2.0/process/executable"));
		Assert.assertTrue(processContent.contains("<bpel:import location=\"EmptyProcessArtifacts.wsdl\""));
		Assert.assertTrue(processContent.contains("<bpel:sequence name=\"main\">"));
		Assert.assertTrue(processContent.contains("<bpel:empty name=\"Empty\"></bpel:empty>"));
		
		Assert.assertFalse(processContent.contains("<bpel:receive name=\"receiveInput\""));
		
		Assert.assertTrue(isRuntimeSet("EmptyProcessProject"));
	}

	@Test
	public void createNewAbstractSyncProcess() throws Exception {
		IProject project = createNewProject("AbstractProcessProject");
		IFile process =    createNewProcess("AbstractProcessProject", "AbstractProcess", BPELConstants.SYNC_PROCESS_LABEL, true);
		
		process.getFileExtension();
		String processContent = loadFile(process);
		
		Assert.assertTrue(processContent != null);
		Assert.assertTrue(processContent.contains("http://docs.oasis-open.org/wsbpel/2.0/process/abstract"));
		Assert.assertTrue(processContent.contains("<bpel:import location=\"AbstractProcessArtifacts.wsdl\""));
		Assert.assertTrue(processContent.contains("<bpel:receive name=\"receiveInput\""));
		Assert.assertTrue(processContent.contains("<bpel:reply name=\"replyOutput\""));
		Assert.assertTrue(isRuntimeSet("AbstractProcessProject"));
	}
	
	
	/**
	 * @author psrna
	 * @throws Exception
	 */
	@Test
	public void createNewDeployDescriptor() throws Exception {
		
		IProject project = createNewProject("ODEProject");
		IFile deploy = createNewDeployDescriptor("ODEProject");
	
		String deployContent = loadFile(deploy);
		Assert.assertTrue(deployContent != null);
		
	}
	
	/**
	 * @author apodhrad
	 * 
	 * Test for JBIDE-11536
	 */
	@Test
	public void createNewRuntime() {
		// New Project
		bot.menu("File").menu("New").menu("Project...").click();
		bot.shell("New Project").activate();

		// Choose BPEL 2.0 project
		SWTBotTree tree = bot.tree();
		tree.expandNode("BPEL 2.0").expandNode("BPEL Project").select();
		assertTrue(bot.button("Next >").isEnabled());
		bot.button("Next >").click();
		
		// Create new runtime
		bot.shell("New BPEL Project").activate();
		bot.button("New Runtime...").click();
		bot.shell("New Server Runtime Environment").activate();
		
		tree = bot.tree();
		assertTrue("There is no server adapter available.", tree.getAllItems().length > 0);
		
		TableCollection selection = bot.tree().selection();
		assertTrue("No server has been selected as default.", selection.rowCount() > 0);
		
		// Is the checkbox ok?
		assertTrue(bot.checkBox("Create a new local server").isVisible());
		bot.checkBox("Create a new local server").select();
		assertTrue(bot.checkBox("Create a new local server").isVisible());
		
		assertTrue(bot.button("Next >").isEnabled());
		bot.button("Next >").click();
		String serverName = bot.textWithLabel("Name").getText();
		assertTrue(bot.button("Finish").isEnabled());
		bot.button("Finish").click();
		
		assertEquals(bot.comboBoxInGroup("Target runtime").getText(), serverName);
		
		assertTrue(bot.button("Cancel").isEnabled());
		bot.button("Cancel").click();
		
		SWTBotView serversView = bot.viewByTitle("Servers");
		serversView.show();
		serversView.setFocus();
		
		// check if the new server is available
		tree = serversView.bot().tree();
		SWTBotTreeItem[] servers = tree.getAllItems();
		SWTBotTreeItem server = null;
		for (int i = 0; i < servers.length; i++) {
			if(servers[i].getText().contains(serverName)) {
				server = servers[i];
				break;
			}
		}
		assertNotNull("The new server runtime not found", server);
		log.info("New server runtime was created: " + serverName);
		
		// delete the server
		SWTBot wiz = open.preferenceOpen(ActionItem.Preference.ServerRuntimeEnvironments.LABEL);
		SWTBotTable tbRuntimeEnvironments = wiz.table();
		SWTBotTableItem serverRuntime = tbRuntimeEnvironments.getTableItem(serverName);
		serverRuntime.select();
		wiz.button("Remove").click();
		wiz.button("OK").click();
		
		
//		ContextMenuHelper.prepareTreeItemForContextMenu(tree, server);
//		new SWTBotMenu(ContextMenuHelper.getContextMenu(tree, IDELabel.Menu.DELETE, false)).click();
//		SWTBotShell shell = bot.waitForShell("Delete Server");
//		shell.bot().button("OK").click();
		server = null;
		for (int i = 0; i < servers.length; i++) {
			if(servers[i].getText().contains(serverName)) {
				server = servers[i];
				break;
			}
		}
		assertNull("The server wasn't deleted", server);
		log.info("The server runtime was deleted: " + serverName);
	}
	
	boolean isRuntimeSet(String projectName) throws Exception {
		SWTBotView projectExplorer =  bot.viewByTitle("Project Explorer");
		projectExplorer.setFocus();
		
		// diaplay Project Properties
		SWTBotTree tree = projectExplorer.bot().tree().select(projectName);
		tree.getTreeItem(projectName).contextMenu("Properties").click();

		SWTBotShell shell = bot.shell("Properties for " + projectName).activate();
		
		bot.tree().select("Targeted Runtimes");
		bot.checkBox("Show &all runtimes").select();
		boolean hasRuntime = bot.table().containsItem(configuredState.getServer().name); 
		shell.close();
		
		return hasRuntime;
	}
	
	
}
