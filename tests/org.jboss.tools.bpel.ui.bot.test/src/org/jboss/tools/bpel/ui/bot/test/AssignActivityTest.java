package org.jboss.tools.bpel.ui.bot.test;

import org.eclipse.swtbot.eclipse.gef.finder.SWTGefBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;
import org.jboss.tools.bpel.ui.bot.ext.widgets.BotBpelEditor;
import org.jboss.tools.bpel.ui.bot.test.util.ResourceHelper;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@Require(clearProjects = true, server = @Server(type = ServerType.ALL, state = ServerState.Running), perspective="BPEL")
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
	
	@Before
	public void setupWorkspace() throws Exception {
		projectExplorer.deleteAllProjects();
		// Need to use own importer. ResourceUtils does not import the project correctly when server
		// is running. TODO: Why?
		log.info("APLog: test");
		ResourceHelper.importProject(BUNDLE, "/projects/AssignerProject", "AssignerProject");
		projectExplorer.selectProject("AssignerProject");
	}

	@After
	public void cleanupWorkspace() throws Exception {
		projectExplorer.deleteAllProjects();
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