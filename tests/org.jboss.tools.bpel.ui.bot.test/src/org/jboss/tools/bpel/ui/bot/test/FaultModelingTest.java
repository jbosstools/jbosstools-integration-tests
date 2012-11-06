package org.jboss.tools.bpel.ui.bot.test;

import org.jboss.tools.bpel.ui.bot.ext.BpelBot;
import org.jboss.tools.bpel.ui.bot.ext.editor.BpelEditor;
import org.jboss.tools.bpel.ui.bot.test.util.ResourceHelper;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.junit.Before;
import org.junit.Test;

/**
 * TODO: finish fault modeling
 * 
 * @author mbaluch
 */
@Require(server = @Server(type = ServerType.ALL, state = ServerState.Disabled), perspective = "BPEL")
public class FaultModelingTest extends BPELTest {

	public static final String PROJECT_NAME = "bpel_scope";
	public static final String BPEL_FILE_NAME = "scope.bpel";

//	IProject project;
//	ServersView sView = new ServersView();
//	PackageExplorer pExplorer = new PackageExplorer();
	
	private BpelBot bpelBot;
	
	public FaultModelingTest() {
		bpelBot = new BpelBot();
	}

	@Before
	public void setupWorkspace() throws Exception {
		ResourceHelper.importProject(Activator.PLUGIN_ID, "/projects/ScopeProcess", PROJECT_NAME);
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
		BpelEditor bpelEditor = bpelBot.openBpelFile(PROJECT_NAME, BPEL_FILE_NAME);
		bpelEditor.activateDesignPage();
		
		bpelEditor.addReceive("receive", "inputMessage", new String[] {"scopeProcess", "scopePT", "processRequest"}, true);
		bpelEditor.addScope("outerScope", false)
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
				
		bpelEditor.select(bpelEditor.getEditPart("outerScope"));
		
		// add FaultHandler
		bpelEditor.addFaultHandler("Exception", "exceptionMessage")
			.addIf("If", "$inputMessage.payload/compensate = 'false'")
				.addRethrow("Rethrow");
		// select faultHandlerPart
		bpelEditor.select(bpelEditor.getSelectedPart().parent());
		bpelEditor.addCompensateScope("CompensateScope", "innerScope");
			
		bpelEditor.select(bpelEditor.getEditPart("outerScope"));
		bpelEditor.addEmpty("DoSomethingInteresting");
		bpelEditor.addIf("if", "$inputMessage.payload/simulateException = 'true'")
			.addThrow("Throw", "propegatedFault");
		
		bpelEditor.select(bpelEditor.getSelectedPart().parent());
		bpelEditor.addAssignVarToExpression("CopyLocalVarToOutputMessage", new String[] {"localVariable : string"}, "$outputMessage.result");
		
		bpelEditor.selectMainEditPart();
		bpelEditor.addReply("Reply", "outputMessage", "", new String[] {"scopeProcess", "scopePT", "processRequest"});
	}	
	

}
