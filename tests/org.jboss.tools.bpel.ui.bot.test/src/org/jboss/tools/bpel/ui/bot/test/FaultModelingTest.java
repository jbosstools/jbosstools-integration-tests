package org.jboss.tools.bpel.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.tools.bpel.reddeer.activity.Assign;
import org.jboss.tools.bpel.reddeer.activity.Catch;
import org.jboss.tools.bpel.reddeer.activity.CompensationHandler;
import org.jboss.tools.bpel.reddeer.activity.FaultHandler;
import org.jboss.tools.bpel.reddeer.activity.If;
import org.jboss.tools.bpel.reddeer.activity.Receive;
import org.jboss.tools.bpel.reddeer.activity.Scope;
import org.jboss.tools.bpel.reddeer.activity.Sequence;
import org.jboss.tools.bpel.reddeer.shell.EclipseShell;
import org.jboss.tools.bpel.reddeer.wizard.ImportProjectWizard;
import org.jboss.tools.bpel.ui.bot.test.suite.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.bpel.ui.bot.test.suite.PerspectiveRequirement.Perspective;
import org.jboss.tools.bpel.ui.bot.test.suite.ServerRequirement.Server;
import org.jboss.tools.bpel.ui.bot.test.suite.ServerRequirement.State;
import org.jboss.tools.bpel.ui.bot.test.suite.ServerRequirement.Type;
import org.jboss.tools.bpel.ui.bot.test.util.ResourceHelper;
import org.junit.Test;

/**
 * 
 * @author apodhrad
 */
@CleanWorkspace
@Perspective(name="BPEL")
@Server(type = Type.ALL, state = State.NOT_RUNNING)
public class FaultModelingTest extends SWTBotTestCase {

	public static final String PROJECT_NAME = "bpel_scope";
	public static final String BPEL_FILE_NAME = "scope.bpel";

	/**
	 * - sequence - scope - flow - throw - rethrow - compensate -
	 * compensateScope
	 * 
	 * @throws Exception
	 */
	@Test
	public void testScopes() throws Exception {
		String projectLocation = ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID,
				"resources/projects/" + PROJECT_NAME + ".zip");
		new ImportProjectWizard(projectLocation).execute();

		new EclipseShell().maximize();

		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		Project project = projectExplorer.getProject(PROJECT_NAME);
		project.getProjectItem("bpelContent", BPEL_FILE_NAME).open();

		Sequence mainSequence = new Sequence("Main");
		mainSequence.addReceive("mainReceive");
		mainSequence.addScope("outerScope");
		mainSequence.addReply("mainReply").pickOperation("processRequest");

		new Receive("mainReceive").pickOperation("processRequest").checkCreateInstance();

		Scope outerScope = new Scope("outerScope");
		outerScope.addAssign("outerAssign1");
		outerScope.addScope("innerScope");
		outerScope.addEmpty("outerEmpty");
		If outerIf = outerScope.addIf("outerIf");
		outerIf.setCondition("$inputMessage.payload/simulateException = 'true'");
		outerIf.addThrow("ifThrow").setUserFaultName("ambiguousReceive");
		outerScope.addAssign("outerAssign2");
		FaultHandler faultHandler = outerScope.addFaultHandler();

		Catch faultCatch = new Catch(faultHandler);
		faultCatch.getSequence(null).delete();
		If faultIf = faultCatch.addIf("faultIf");
		faultIf.setCondition("$inputMessage.payload/compensate = 'false'");
		faultIf.addRethrow("faultRethrow");
		faultCatch.addCompensateScope("faultCompensate").setTargetActivity("innerScope");

		Scope innerScope = new Scope("innerScope");
		innerScope.addAssign("innerAssign1");

		CompensationHandler compensatioHandler = innerScope.addCompensationHandler();
		compensatioHandler.getSequence(null).delete();
		compensatioHandler.addFlow("compensationFlow").addSequence("flowSequence");

		Sequence flowSequence = new Sequence("flowSequence");
		flowSequence.addAssign("flowAssign1");
		flowSequence.addAssign("flowAssign2");

		new Assign("outerAssign1").addFixToVar("Outer Init Value", "localVariable : string");
		new Assign("innerAssign1").addFixToVar("Inner Init Value", "localVariable : string");
		new Assign("flowAssign1").addFixToVar("Flow Init Value", "localVariable : string");
		new Assign("outerAssign2").addVarToExp("localVariable : string", "$outputMessage.result");
		new Assign("flowAssign2").addVarToExp("localVariable : string", "$outputMessage.result");

	}
}
