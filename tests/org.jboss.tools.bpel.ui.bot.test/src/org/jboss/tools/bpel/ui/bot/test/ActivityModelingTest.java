package org.jboss.tools.bpel.ui.bot.test;

import org.eclipse.core.resources.IProject;

import org.eclipse.swtbot.eclipse.gef.finder.SWTGefBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;

import org.jboss.tools.bpel.ui.bot.ext.widgets.BotBpelEditor;
import org.jboss.tools.bpel.ui.bot.test.suite.BPELTest;
import org.jboss.tools.bpel.ui.bot.test.util.ResourceHelper;

import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.view.PackageExplorer;
import org.jboss.tools.ui.bot.ext.view.ServersView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@Require(clearProjects = true, /*server = @Server(type = ServerType.SOA, state = ServerState.Present),*/ perspective="BPEL")
public class ActivityModelingTest extends BPELTest {

	static final String BUNDLE = "org.jboss.tools.bpel.ui.bot.test";

	IProject project;
	ServersView sView = new ServersView();
	PackageExplorer pExplorer = new PackageExplorer();

	@Before
	public void setupWorkspace() throws Exception {
		System.err.println("setup start!");
		
		pExplorer.deleteAllProjects();
		
		ResourceHelper.importProject(BUNDLE, "/projects/DiscriminantProcess", "DiscriminantProcess");
		
		//ResourcesUtils.importProject(BUNDLE, "/projects/DiscriminantProcess",
		//		"DiscriminantProcess", null);
		
		bot.viewByTitle("Package Explorer").setFocus();
		pExplorer.selectProject("DiscriminantProcess");
		
		System.err.println("setup complete!");
	
	}

	@After
	public void cleanupWorkspace() throws Exception {
//		pExplorer.deleteAllProjects();
	}

	/**
	 * - if
	 * - pick
	 * - while
	 * - forEach
	 * - repeatUntil
	 * - wait
	 * - empty
	 * - exit
	 * 
	 * @throws Exception
	 */
	@Test
	public void testModeling() throws Exception {
		
		openFile("DiscriminantProcess", "bpelContent", "Discriminant.bpel");
	
		SWTGefBot gefBot = new SWTGefBot();
		final SWTBotGefEditor editor = gefBot.gefEditor("Discriminant.bpel");
		final BotBpelEditor bpel = new BotBpelEditor(editor, gefBot);
		bpel.activatePage("Design");
		
		bpel.addReceive("receive", "DiscriminantRequest", new String[] {"client", "Discriminant", "calculateDiscriminant"}, true);
		bpel.addPick("receiveOnPick", true, "DiscriminantRequest", new String[] {"client", "Discriminant", "calculateDiscriminant"})
			// model pick onMessage
			.addIf("if1", "true() AND true()")
				// model if branch
				.addExit("Quit");
			// model else
			bpel.select(bpel.getSelectedPart().parent());
			bpel.addElse("if1")
				.addInvoke("invokePartner1", "MathRequest1", "MathResponse1", new String[] {"math", "Math", "calculate"});
		
		bpel.select(editor.mainEditPart());
		// model pick onAlarm
		bpel.addOnAlarm("receiveOnPick", "'PT10M'")
			// model while
			.addWhile("while1", "false()")
				// model ForEach
				.addForEach("forEach1", "10", "20")
					.addInvoke("invokePartner2", "MathRequest1", "MathResponse1", new String[] {"math", "Math", "calculate"})
					.addWait("wait", "'PT1S'");
		
		bpel.select(editor.mainEditPart());
		bpel.addRepeatUntil("repeatUntil", "false()")
			// model repeatUntil
			.addEmpty("empty");
		
		bpel.select(editor.mainEditPart());
		bpel.addReply("reply", "DiscriminantResponse", "", new String[] {"client", "Discriminant", "calculateDiscriminant"});
		
	}
	
	void openFile(String projectName, String... path) throws Exception {
		log.info("Opening file: " + path[path.length - 1] + " ...");
		pExplorer.openFile(projectName, path);
	}

}
