package org.jboss.tools.bpel.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.tools.bpel.reddeer.activity.ForEach;
import org.jboss.tools.bpel.reddeer.activity.If;
import org.jboss.tools.bpel.reddeer.activity.OnAlarm;
import org.jboss.tools.bpel.reddeer.activity.OnMessage;
import org.jboss.tools.bpel.reddeer.activity.Pick;
import org.jboss.tools.bpel.reddeer.activity.RepeatUntil;
import org.jboss.tools.bpel.reddeer.activity.Scope;
import org.jboss.tools.bpel.reddeer.activity.Sequence;
import org.jboss.tools.bpel.reddeer.wizard.ImportProjectWizard;
import org.jboss.tools.bpel.ui.bot.test.suite.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.bpel.ui.bot.test.suite.PerspectiveRequirement.Perspective;
import org.jboss.tools.bpel.ui.bot.test.util.ResourceHelper;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author apodhrad
 * 
 */
@CleanWorkspace
@Perspective(name = "BPEL")
public class ActivityModelingTest extends SWTBotTestCase {

	public static final String PROJECT_NAME = "DiscriminantProcess";
	public static final String BPEL_FILE_NAME = "Discriminant.bpel";

	@Before
	public void setupWorkspace() throws Exception {
		String projectLocation = ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID,
				"resources/projects/" + PROJECT_NAME + ".zip");
		new ImportProjectWizard(projectLocation).execute();
	}

	/**
	 * - if - pick - while - forEach - repeatUntil - wait - empty - exit
	 * 
	 * @throws Exception
	 */
	@Test
	public void testModeling() throws Exception {
		new WorkbenchShell().maximize();

		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		Project project = projectExplorer.getProject(PROJECT_NAME);
		project.getProjectItem("bpelContent", BPEL_FILE_NAME).open();

		Sequence mainSequence = new Sequence("Main");
		mainSequence.addReceive("receive").pickOperation("calculateDiscriminant")
				.checkCreateInstance();
		Pick pick = mainSequence.addPick("receiveOnPick");
		// the following line causes xulrunner problem on ubuntu
		// mainSequence.addRepeatUntil("repeat1").setCondition("false()").addEmpty("empty1");
		mainSequence.addRepeatUntil("repeat1").setCondition("false()");
		mainSequence.addReply("reply").pickOperation("calculateDiscriminant");

		new RepeatUntil("repeat1").addInvoke("invokePartner3").pickOperation("calculate");

		new OnMessage(pick).pickOperation("calculateDiscriminant").addIf("if1");
		new If("if1").setCondition("true() AND true()").addExit("Quit");
		new If("if1").addElse().addInvoke("invokePartner1").pickOperation("calculate");

		OnAlarm onAlarm = pick.addOnAlarm().setCondition("'PT10M'", "Date");
		onAlarm.getScope().delete();
		onAlarm.addWhile("while1").setCondition("true()").addForEach("forEach1");

		Scope scope = new ForEach("forEach1").setCounterValue("10", "20").getScope();
		scope.addInvoke("invokePartner2").pickOperation("calculate");
		scope.addWait("wait1").setCondition("'PT1S'", "Date");
	}

}
