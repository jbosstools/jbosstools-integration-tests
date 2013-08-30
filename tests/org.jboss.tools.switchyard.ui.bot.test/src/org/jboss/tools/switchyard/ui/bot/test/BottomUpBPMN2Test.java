package org.jboss.tools.switchyard.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.swt.condition.TableHasRows;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.switchyard.reddeer.component.Component;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.editor.TextEditor;
import org.jboss.tools.switchyard.reddeer.wizard.HTTPBindingWizard;
import org.jboss.tools.switchyard.reddeer.wizard.ImportFileWizard;
import org.jboss.tools.switchyard.reddeer.wizard.NewServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.PromoteServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.SwitchYardProjectWizard;
import org.jboss.tools.switchyard.ui.bot.test.suite.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.switchyard.ui.bot.test.suite.PerspectiveRequirement.Perspective;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerDeployment;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.Server;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.State;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.Type;
import org.jboss.tools.switchyard.ui.bot.test.suite.SwitchyardSuite;
import org.jboss.tools.switchyard.ui.bot.test.util.HttpClient;
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
public class BottomUpBPMN2Test extends SWTBotTestCase {

	public static final String PROJECT = "bpmn2_project";
	public static final String PACKAGE = "com.example.switchyard.bpmn2_project";
	public static final String BPMN2_FILE = "sample.bpmn";
	public static final String JAVA_FILE = "MyHttpMessageComposer";

	@Before
	public void closeSwitchyardFile() {
		try {
			new SwitchYardEditor().saveAndClose();
		} catch (Exception ex) {
			// it is ok, we just try to close switchyard.xml if it is open
		}
	}

	@Test
	public void bottomUpCamelTest() throws Exception {
		new SwitchYardProjectWizard(PROJECT).impl("BPM (jBPM)").binding("HTTP").create();
		Project project = new ProjectExplorer().getProject(PROJECT);

		// Import BPMN process
		project.getProjectItem("src/main/java", PACKAGE).select();
		new ImportFileWizard().importFile("resources/bpmn", BPMN2_FILE);

		// Import HTTP message composer
		project.getProjectItem("src/main/java", PACKAGE).select();
		new ImportFileWizard().importFile("resources/java", JAVA_FILE + ".java");

		// Edit the composer
		project.getProjectItem("src/main/java", PACKAGE, JAVA_FILE + ".java").open();
		new TextEditor(JAVA_FILE + ".java").deleteLineWith("package")
				.type("package " + PACKAGE + ";").saveAndClose();

		// Add component
		new SwitchYardEditor().addComponent("Component");
		new Component("Component").select();
		new SwitchYardEditor().activateTool("Process (BPMN)");
		new Component("Component").click();

		// Select existing implementation
		new PushButton("Browse...").click();
		Bot.get().shell("Select Resource").activate();
		new DefaultText(0).setText(BPMN2_FILE);
		new WaitUntil(new TableHasRows(new DefaultTable()));
		new PushButton("OK").click();
		new PushButton("Finish").click();

		// Create new service and interface
		new Component("Component").contextButton("Service").click();
		new NewServiceWizard().createJavaInterface("Hello").activate().finish();

		// Edit the interface
		new Service("Hello").doubleClick();
		new TextEditor("Hello.java").typeAfter("Hello", "String sayHello(String name);")
				.saveAndClose();

		// Edit the BPMN process
		new Component("Component").hover();
		new Component("Component").click();
		new Component("Component").contextButton("Properties").click();

		new DefaultTreeItem("Implementation").select();

		new LabeledText("Process ID:").setText("com.sample.bpmn.hello");

		new DefaultTabItem("Operations").activate();
		new PushButton("Add").click();

		Bot.get().table(0).click(0, 0);
		new DefaultText(1).setText("sayHello");
		new DefaultTable(0).select(0);

		new PushButton(2, "Add").click();
		Bot.get().table(2).click(0, 1);
		new DefaultText(1).setText("name");
		new DefaultTable(0).select(0);

		new PushButton(3, "Add").click();
		Bot.get().table(3).click(0, 0);
		new DefaultText(1).setText("result");
		new DefaultTable(0).select(0);

		new PushButton("OK").click();

		PromoteServiceWizard wizard = new Service("Hello").promoteService();
		wizard.activate().setServiceName("HelloService").finish();

		// Add HTTP binding
		new Service("HelloService").addBinding("HTTP");
		HTTPBindingWizard httpWizard = new HTTPBindingWizard();
		httpWizard.setContextpath(PROJECT);
		httpWizard.setOperation("sayHello");
		httpWizard.next();
		httpWizard.setMessageComposer(JAVA_FILE);
		httpWizard.finish();

		new SwitchYardEditor().save();

		// Deploy and test the project
		new ServerDeployment().deployProject(PROJECT);
		String url = "http://localhost:8080/" + PROJECT;
		HttpClient httpClient = new HttpClient(url);
		assertEquals("Hello apodhrad", httpClient.send("apodhrad"));
		assertEquals("Hello JBoss", httpClient.send("JBoss"));

		new WaitUntil(new ConsoleHasText("Hello JBoss"));
	}
}
