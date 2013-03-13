package org.jboss.tools.bpel.ui.bot.test;

import org.jboss.tools.bpel.ui.bot.ext.activity.ForEach;
import org.jboss.tools.bpel.ui.bot.ext.activity.If;
import org.jboss.tools.bpel.ui.bot.ext.activity.OnAlarm;
import org.jboss.tools.bpel.ui.bot.ext.activity.OnMessage;
import org.jboss.tools.bpel.ui.bot.ext.activity.Pick;
import org.jboss.tools.bpel.ui.bot.ext.activity.RepeatUntil;
import org.jboss.tools.bpel.ui.bot.ext.activity.Scope;
import org.jboss.tools.bpel.ui.bot.ext.activity.Sequence;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.helper.ImportHelper;
import org.jboss.tools.ui.bot.ext.helper.ResourceHelper;
import org.junit.Before;

/**
 * 
 * @author apodhrad
 * 
 */
@Require(perspective = "BPEL")
public class ActivityModelingTest extends SWTTestExt {

	public static final String PROJECT_NAME = "DiscriminantProcess";
	public static final String BPEL_FILE_NAME = "Discriminant.bpel";

	@Before
	public void setupWorkspace() throws Exception {
		String projectLocation = ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID,
				"resources/projects/" + PROJECT_NAME + ".zip");
		ImportHelper.importProjectFromZip(projectLocation);
	}

	/**
	 * - if - pick - while - forEach - repeatUntil - wait - empty - exit
	 * 
	 * @throws Exception
	 */
	// @Test
	public void testModeling() throws Exception {
		eclipse.maximizeActiveShell();

		projectExplorer.openFile(PROJECT_NAME, "bpelContent", BPEL_FILE_NAME);

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
