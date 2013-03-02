package org.jboss.tools.bpel.ui.bot.test;

import org.jboss.tools.bpel.ui.bot.ext.activity.Assign;
import org.jboss.tools.bpel.ui.bot.ext.activity.Catch;
import org.jboss.tools.bpel.ui.bot.ext.activity.CompensationHandler;
import org.jboss.tools.bpel.ui.bot.ext.activity.FaultHandler;
import org.jboss.tools.bpel.ui.bot.ext.activity.If;
import org.jboss.tools.bpel.ui.bot.ext.activity.Receive;
import org.jboss.tools.bpel.ui.bot.ext.activity.Scope;
import org.jboss.tools.bpel.ui.bot.ext.activity.Sequence;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.helper.ImportHelper;
import org.jboss.tools.ui.bot.ext.helper.ResourceHelper;
import org.junit.Test;

/**
 * 
 * @author apodhrad
 */
@Require(server = @Server(state = ServerState.Disabled), perspective = "BPEL")
public class FaultModelingTest extends SWTTestExt {

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
		ImportHelper.importProjectFromZip(projectLocation);

		eclipse.maximizeActiveShell();

		projectExplorer.openFile(PROJECT_NAME, "bpelContent", BPEL_FILE_NAME);

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
