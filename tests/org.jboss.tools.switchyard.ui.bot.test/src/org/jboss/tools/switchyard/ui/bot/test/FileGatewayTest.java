package org.jboss.tools.switchyard.ui.bot.test;

import java.io.File;
import java.io.FileWriter;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.switchyard.reddeer.component.Bean;
import org.jboss.tools.switchyard.reddeer.component.Component;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.editor.TextEditor;
import org.jboss.tools.switchyard.reddeer.wizard.FileBindingWizard;
import org.jboss.tools.switchyard.reddeer.wizard.SwitchYardProjectWizard;
import org.jboss.tools.switchyard.ui.bot.test.suite.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.switchyard.ui.bot.test.suite.PerspectiveRequirement.Perspective;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerDeployment;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.Server;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.State;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.Type;
import org.jboss.tools.switchyard.ui.bot.test.suite.SwitchyardSuite;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Creation test from existing BPM process
 * 
 * @author apodhrad
 * 
 */
@CleanWorkspace
@Perspective(name = "Java EE")
@Server(type = Type.ALL, state = State.RUNNING)
@RunWith(SwitchyardSuite.class)
public class FileGatewayTest extends SWTBotTestCase {

	public static final String PROJECT = "file_project";
	public static final String PACKAGE = "com.example.switchyard.file_project";

	@Before
	public void closeSwitchyardFile() {
		try {
			new SwitchYardEditor().saveAndClose();
		} catch (Exception ex) {
			// it is ok, we just try to close switchyard.xml if it is open
		}
	}

	@Test
	public void fileGatewayTest() throws Exception {
		new SwitchYardProjectWizard(PROJECT).impl("Bean").binding("File").create();

		// Create new service and interface
		new Bean().setService("Info").create();

		// Edit the interface
		new Component("Info").doubleClick();
		new TextEditor("Info.java").typeAfter("interface", "void printInfo(String body);")
				.saveAndClose();

		// Edit the bean
		new Component("InfoBean").doubleClick();
		new TextEditor("InfoBean.java").typeAfter("public class", "@Override").newLine()
				.type("public void printInfo(String body) {").newLine()
				.type("System.out.println(\"Body: \" + body);}").saveAndClose();

		new SwitchYardEditor().save();

		// Promote info service
		new Service("Info").promoteService().activate().setServiceName("InfoService").finish();

		// Add File binding
		new Service("InfoService").addBinding("File");
		FileBindingWizard wizard = new FileBindingWizard();
		String path = new File("target/input").getAbsolutePath();
		wizard.setDirAutoCreation(true).setMoveDirectory("processed");
		wizard.setDirectory(path);

		// It's needed to change focuses between text inputs
		SWTWorkbenchBot bot = new SWTWorkbenchBot();
		bot.textWithLabel("Directory*").setFocus();
		bot.textWithLabel("Move (Default .camel)").setFocus();
		bot.textWithLabel("Name").setFocus();
		bot.textWithLabel("File Name").setFocus();

		// Now, we can click finish button
		wizard.finish();

		new SwitchYardEditor().save();

		// Deploy and test the project
		new ServerDeployment().deployProject(PROJECT);

		FileWriter out = new FileWriter(path + "/test.txt");
		out.write("Hello File Gateway");
		out.flush();
		out.close();

		new WaitUntil(new ConsoleHasText("Body: Hello File Gateway"));

		File file = new File(path + "/processed/test.txt");
		assertTrue("File 'test.txt' wasn't processed", file.exists());
	}
}
