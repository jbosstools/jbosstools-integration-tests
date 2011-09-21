package org.jboss.tools.bpel.ui.bot.test;

import org.eclipse.core.resources.IProject;

import org.eclipse.swtbot.eclipse.gef.finder.SWTGefBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;

import org.jboss.tools.bpel.ui.bot.ext.widgets.BotBpelEditor;
import org.jboss.tools.bpel.ui.bot.test.suite.BPELTest;
import org.jboss.tools.bpel.ui.bot.test.util.ResourceHelper;
import org.jboss.tools.bpel.util.SendSoapMessage;

import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.types.ViewType;
import org.jboss.tools.ui.bot.ext.view.PackageExplorer;
import org.jboss.tools.ui.bot.ext.view.ServersView;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@Require(clearProjects = true, server = @Server(type = ServerType.SOA, state = ServerState.Running), perspective="BPEL")
public class AssignActivityTest extends BPELTest {

	static String BUNDLE   = "org.jboss.tools.bpel.ui.bot.test";
	static String ENDPOINT = "http://localhost:8080/AssignTestProcess";
	static String MESSAGE  = 
		"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
		"	<soapenv:Header/>" +
		"	<soapenv:Body>" +
		"		<payload>Initial value</payload>" +
		"	</soapenv:Body>" +
		"</soapenv:Envelope>";
	
	
	
	IProject project;
	ServersView sView = new ServersView();
	PackageExplorer pExplorer = new PackageExplorer() {

		@Override
		public void runOnServer(String projectName) {
			String serverName = AssignActivityTest.configuredState.getServer().name;
			serverName = "SOA-5.1"; // remove me !!!

			bot.viewByTitle("Servers").show();
			bot.viewByTitle("Servers").setFocus();
			
			SWTBotTree tree = bot.viewByTitle("Servers").bot().tree(); 
			SWTBotTreeItem server = tree.getTreeItem(serverName + "  [Started, Synchronized]").select();
			
			ContextMenuHelper.prepareTreeItemForContextMenu(tree, server);
			new SWTBotMenu(ContextMenuHelper.getContextMenu(tree, IDELabel.Menu.ADD_AND_REMOVE, false)).click();
			
			SWTBotShell shell = AssignActivityTest.bot.shell("Add and Remove...");
			shell.activate();
			
			SWTBot viewBot = shell.bot();
			// The list in the "Add and Remove..." dialog is a Tree !!! see EclipseSpy ...
			viewBot.tree().setFocus();
			viewBot.tree().select(projectName);
			viewBot.button("Add >").click();
			viewBot.button("Finish").click();
		}

	};

	@Before
	public void setupWorkspace() throws Exception {
		pExplorer.deleteAllProjects();
		// Need to use own importer. ResourceUtils does not import the project correctly when server
		// is running. TODO: Why?
		ResourceHelper.importProject(BUNDLE, "/projects/AssignerProject", "AssignerProject");
		bot.viewByTitle("Package Explorer").setFocus();
		pExplorer.selectProject("AssignerProject");
	}

	@After
	public void cleanupWorkspace() throws Exception {
		pExplorer.deleteAllProjects();
	}

	
//	/**
//	 * TODO: assert that the Variable Initializer dialog will not be shown
//	 * @throws Exception
//	 */
//	@Test
//	public void testLocalVarAssignment() throws Exception {
//		// Create the process
//		openFile("AssignerProject", "bpelContent", "AssignTestProcess.bpel");
//	
//		SWTGefBot gefBot = new SWTGefBot();
//		final SWTBotGefEditor editor = gefBot.gefEditor("AssignTestProcess.bpel");
//		final BotBpelEditor bpel = new BotBpelEditor(editor, gefBot);
//
//		bpel.addReceive("receiveSimple", "simpleIn", new String[] {"client", "AssignTestProcess", "simple"}, true);
//		bpel.addAssignVarToVar("assignToLocal", new String[] {"simpleIn : simpleRequestMessage", "payload : string"}, new String[] {"processVar : string"});
////		Assert.assertFalse(console.getConsoleText().contains("Failed to execute runnable (java.lang.NullPointerException)"));
//	}
	
	
	@Test
	public void testAssignment() throws Exception {
		// Create the process
		openFile("AssignerProject", "bpelContent", "AssignTestProcess.bpel");
	
		SWTGefBot gefBot = new SWTGefBot();
		final SWTBotGefEditor editor = gefBot.gefEditor("AssignTestProcess.bpel");
		final BotBpelEditor bpel = new BotBpelEditor(editor, gefBot);
		bpel.activatePage("Design");
		
		bpel.addReceive("receiveSimple", "simpleIn", new String[] {"client", "AssignTestProcess", "simple"}, true);
		bpel.addAssignVarToVar("assignSimpleToSimple", 
				new String[] {"simpleIn : simpleRequestMessage", "payload : string"}, 
				new String[] {"simpleOut : simpleResponseMessage", "payload : string"}
		);
		
		bpel.addAssignVarToVar("assignSimpleToComplex", 
				new String[] {"simpleIn : simpleRequestMessage", "payload : string"}, 
				new String[] {"complexOut : complexResponseMessage", "complexResponse : complexResponse", "result : string"}
		);
		
		bpel.addAssignVarToVar("assignSimpleToModerate", 
				new String[] {"simpleIn : simpleRequestMessage", "payload : string"}, 
				new String[] {"moderateOut : moderateResponseMessage", "moderateResponse : complexResponseType", "result : string"}
		);
		
		bpel.addAssignExpressionToExpression("assignExpressionToExpression", "$simpleIn.payload", "$moderateOut.moderateResponse/result");
		bpel.addAssignFixedToExpression("assignFixedToExpression", "Fixed Expression", "$simpleOut.payload");
		
		bpel.addReply("replySimple", "simpleOut", "", new String[] {"client", "AssignTestProcess", "simple"});
		/*
		// Publish the process
		pExplorer.runOnServer("AssignerProject");
		Thread.sleep(TIME_5S);
		Assert.assertFalse(console.getConsoleText().contains("DEPLOYMENTS IN ERROR:"));

		// Test the process
		String response = SendSoapMessage.sendMessage(ENDPOINT, MESSAGE, "simple");
		log.info("Got response from process: " + response);
		Thread.sleep(TIME_5S);
		Assert.assertFalse(console.getConsoleText().contains("[ASSIGN] Assignment Fault:"));
		Assert.assertEquals("Fixed Expression", response);
		*/

	}
	


}