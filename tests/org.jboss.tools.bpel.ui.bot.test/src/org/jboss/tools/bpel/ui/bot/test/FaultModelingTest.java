package org.jboss.tools.bpel.ui.bot.test;

import org.eclipse.core.resources.IProject;

import org.eclipse.swtbot.eclipse.gef.finder.SWTGefBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;

import org.jboss.tools.bpel.ui.bot.ext.widgets.BotBpelEditor;
import org.jboss.tools.bpel.ui.bot.test.suite.BPELTest;
import org.jboss.tools.bpel.ui.bot.test.util.ResourceHelper;

import org.jboss.tools.ui.bot.ext.config.Annotations.SWTBotTestRequires;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.view.PackageExplorer;
import org.jboss.tools.ui.bot.ext.view.ServersView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * TODO: finish fault modeling
 * 
 * @author mbaluch
 */
@SWTBotTestRequires(clearProjects = true, server = @Server(type = ServerType.SOA, state = ServerState.Disabled))
public class FaultModelingTest extends BPELTest {

	static String BUNDLE = "org.jboss.tools.bpel.ui.bot.test";

	IProject project;
	ServersView sView = new ServersView();
	PackageExplorer pExplorer = new PackageExplorer();

	@Before
	public void setupWorkspace() throws Exception {
		pExplorer.deleteAllProjects();
		ResourceHelper.importProject(BUNDLE, "/projects/ScopeProcess",
				"bpel_scope");
		
		bot.viewByTitle("Package Explorer").setFocus();
		pExplorer.selectProject("bpel_scope");
	}

	@After
	public void cleanupWorkspace() throws Exception {
//		pExplorer.deleteAllProjects();
	}

	
	/**
	 * - sequence
	 * - scope 
	 * - flow
	 * - throw
	 * - rethrow
	 * - compensate
	 * - compensateScope
	 * 
	 * @throws Exception
	 */
	@Test
	public void testScopes() throws Exception {
		openFile("bpel_scope", "bpelContent", "scope.bpel");
		
		SWTGefBot gefBot = new SWTGefBot();
		SWTBotGefEditor editor = gefBot.gefEditor("scope.bpel");
		BotBpelEditor bpel = new BotBpelEditor(editor, gefBot);
		bpel.activatePage("Design");
		
		bpel.addReceive("receive", "inputMessage", new String[] {"scopeProcess", "scopePT", "processRequest"}, true);
		bpel.addScope("outerScope", false)
			// model scope
			.addAssignFixedToVar("AssignInitialValue", "Initial variable value", new String[] {"localVariable : string"})
			.addScope("innerScope", false)
				// model inner scope
				.addAssignFixedToVar("AssignInnerScopeValue", "Inner scope value", new String[] {"localVariable : string"})
				// model compensation handler
				.addCompensationHandler()
				.addFlow("Flow")
					.addSequence("Sequence")
						.addAssignFixedToVar("AssignCompensatedValue", "Compensated value", new String[] {"localVariable : string"})
						.addAssignVarToExpression("CopyLocalVarToOutputMessage", new String[] {"localVariable : string"}, "$outputMessage.result");
				
		bpel.select(bpel.getEditPart(editor.mainEditPart(), "outerScope"));
		
		// add FaultHandler
		bpel.addFaultHandler("Exception", "exceptionMessage")
			.addIf("If", "$inputMessage.payload/compensate = 'false'")
				.addRethrow("Rethrow");
		// select faultHandlerPart
		bpel.select(bpel.getSelectedPart().parent());
		bpel.addCompensateScope("CompensateScope", "innerScope");
			
		bpel.select(bpel.getEditPart(editor.mainEditPart(), "outerScope"));
		bpel.addEmpty("DoSomethingInteresting");
		bpel.addIf("if", "$inputMessage.payload/simulateException = 'true'")
			.addThrow("Throw", "propegatedFault");
		
		bpel.select(bpel.getSelectedPart().parent());
		bpel.addAssignVarToExpression("CopyLocalVarToOutputMessage", new String[] {"localVariable : string"}, "$outputMessage.result");
		
		bpel.select(editor.mainEditPart());
		bpel.addReply("Reply", "outputMessage", "", new String[] {"scopeProcess", "scopePT", "processRequest"});
	}	
	
	
	void openFile(String projectName, String... path) throws Exception {
		log.info("Opening file: " + path[path.length - 1] + " ...");
		pExplorer.openFile(projectName, path);
	}

}
