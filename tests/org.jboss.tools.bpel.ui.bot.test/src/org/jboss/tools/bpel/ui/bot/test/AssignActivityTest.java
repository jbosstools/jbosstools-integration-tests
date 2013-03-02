package org.jboss.tools.bpel.ui.bot.test;

import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.bpel.ui.bot.ext.activity.Sequence;
import org.jboss.tools.bpel.ui.bot.ext.condition.NoErrorExists;
import org.jboss.tools.bpel.ui.bot.ext.editor.BpelEditor;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.helper.ImportHelper;
import org.jboss.tools.ui.bot.ext.helper.ResourceHelper;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

@Require(perspective = "BPEL")
public class AssignActivityTest extends SWTTestExt {

	@BeforeClass
	public static void maximizeEclipse() throws Exception {
		eclipse.maximizeActiveShell();
	}

	@After
	public void saveAndClose() {
		new BpelEditor().saveAndClose();
	}

	@Test
	public void testAssignment() throws Exception {
		new BPELProject("AssignerProject").openBpelProcess("AssignTestProcess");

		// variables
		String[] simpleIn = new String[] { "simpleIn : simpleRequestMessage", "payload : string" };
		String[] simpleOut = new String[] { "simpleOut : simpleResponseMessage", "payload : string" };
		String[] complexOut = new String[] { "complexOut : complexResponseMessage",
				"complexResponse : complexResponse", "result : string" };
		String[] moderateOut = new String[] { "moderateOut : moderateResponseMessage",
				"moderateResponse : complexResponseType", "result : string" };

		Sequence main = new Sequence("main");
		main.addReceive("receiveSimple").pickOperation("simple").checkCreateInstance();
		main.addAssign("assignSimpleToSimple").addVarToVar(simpleIn, simpleOut);
		main.addAssign("assignSimpleToComplex").addVarToVar(simpleIn, complexOut);
		main.addAssign("assignSimpleToModerate").addVarToVar(simpleIn, moderateOut);
		main.addAssign("assignExpToExp").addExpToExp("$simpleIn.payload",
				"$moderateOut.moderateResponse/result");
		main.addAssign("assignFixToExp").addFixToExp("Fixed Expression", "$simpleOut.payload");
		main.addReply("replySimple").pickOperation("simple");

		new WaitUntil(new NoErrorExists(), TimePeriod.NORMAL);
	}

	@Test
	public void testAssignment2() {
		new BPELProject("DiscriminantProcess").openBpelProcess("Discriminant");

		// variables
		String[] discriminantA = new String[] { "DiscriminantRequest : DiscriminantRequestMessage",
				"parameters : DiscriminantRequest", "a : decimal" };
		String[] discriminantB = new String[] { "DiscriminantRequest : DiscriminantRequestMessage",
				"parameters : DiscriminantRequest", "b : decimal" };
		String[] discriminantResult = new String[] {
				"DiscriminantResponse : DiscriminantResponseMessage",
				"parameters : DiscriminantResponse", "result : int" };
		String[] mathA = new String[] { "MathRequest1 : MathRequestMessage",
				"parameters : MathRequest", "a : decimal" };
		String[] mathB = new String[] { "MathRequest1 : MathRequestMessage",
				"parameters : MathRequest", "b : decimal" };
		String[] mathOperator = new String[] { "MathRequest1 : MathRequestMessage",
				"parameters : MathRequest", "operator : string" };
		String[] mathResult = new String[] { "MathResponse1 : MathResponseMessage",
				"parameters : MathResponse", "result : decimal" };

		Sequence main = new Sequence("Main");
		main.addReceive("receive").pickOperation("calculateDiscriminant").checkCreateInstance();
		main.addAssign("assignRequest1").addVarToVar(discriminantA, mathA);
		main.addAssign("assignRequest2").addVarToVar(discriminantB, mathB);
		main.addAssign("assignRequest3").addFixToVar("*", mathOperator);
		main.addValidate("validateInput").addVariable("DiscriminantRequest");
		main.addInvoke("invokePartner").pickOperation("calculate");
		main.addAssign("assignRequest4").addVarToVar(mathResult, discriminantResult);
		main.addReply("reply").pickOperation("calculateDiscriminant");

		new WaitUntil(new NoErrorExists(), TimePeriod.NORMAL);
	}

	private class BPELProject {

		private String projectName;

		public BPELProject(String projectName) {
			this.projectName = projectName;
			String projectLocation = ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID,
					"resources/projects/" + projectName + ".zip");
			ImportHelper.importProjectFromZip(projectLocation);
		}

		public void open(String fileName) {
			projectExplorer.openFile(projectName, "bpelContent", fileName);
		}

		public void openBpelProcess(String bpelProcess) {
			open(bpelProcess + ".bpel");
		}
	}

}