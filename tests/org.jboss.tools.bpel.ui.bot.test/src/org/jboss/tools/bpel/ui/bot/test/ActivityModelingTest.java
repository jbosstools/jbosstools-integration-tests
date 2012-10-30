package org.jboss.tools.bpel.ui.bot.test;

import org.jboss.tools.bpel.ui.bot.ext.BpelBot;
import org.jboss.tools.bpel.ui.bot.ext.editor.BpelEditor;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.helper.ImportHelper;
import org.jboss.tools.ui.bot.ext.helper.ResourceHelper;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author mbaluch
 * @author apodhrad
 * 
 */
@Require(server = @Server(state = ServerState.Present), perspective = "BPEL")
public class ActivityModelingTest extends SWTTestExt {

	public static final String PROJECT_NAME = "DiscriminantProcess";
	public static final String BPEL_FILE_NAME = "Discriminant.bpel";

	private BpelBot bpelBot;

	public ActivityModelingTest() {
		bpelBot = new BpelBot();
	}

	@Before
	public void setupWorkspace() throws Exception {
		String projectLocation = ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID,
				"projects/" + PROJECT_NAME);
		ImportHelper.importProject(projectLocation);
	}

	/**
	 * - if - pick - while - forEach - repeatUntil - wait - empty - exit
	 * 
	 * @throws Exception
	 */
	@Test
	public void testModeling() throws Exception {
		BpelEditor bpelEditor = bpelBot.openBpelFile(PROJECT_NAME, BPEL_FILE_NAME);
		bpelEditor.activateDesignPage();

		bpelEditor.addReceive("receive", "DiscriminantRequest", new String[] { "client",
				"Discriminant", "calculateDiscriminant" }, true);
		bpelEditor
				.addPick("receiveOnPick", true, "DiscriminantRequest",
						new String[] { "client", "Discriminant", "calculateDiscriminant" })
				// model pick onMessage
				.addIf("if1", "true() AND true()")
				// model if branch
				.addExit("Quit");
		// model else
		bpelEditor.select(bpelEditor.getSelectedPart().parent());
		bpelEditor.addElse("if1").addInvoke("invokePartner1", "MathRequest1", "MathResponse1",
				new String[] { "math", "Math", "calculate" });

		bpelEditor.selectMainEditPart();
		// model pick onAlarm
		bpelEditor.addOnAlarm("receiveOnPick", "'PT10M'")
				// model while
				.addWhile("while1", "false()")
				// model ForEach
				.addForEach("forEach1", "10", "20")
				.addInvoke("invokePartner2", "MathRequest1", "MathResponse1",
						new String[] { "math", "Math", "calculate" }).addWait("wait", "'PT1S'");

		bpelEditor.selectMainEditPart();
		bpelEditor.addRepeatUntil("repeatUntil", "false()")
		// model repeatUntil
				.addEmpty("empty");

		bpelEditor.selectMainEditPart();
		bpelEditor.addReply("reply", "DiscriminantResponse", "", new String[] { "client",
				"Discriminant", "calculateDiscriminant" });

	}

}
