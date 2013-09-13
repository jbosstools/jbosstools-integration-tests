package org.jboss.tools.switchyard.ui.bot.test;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.swt.condition.TableHasRows;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.switchyard.reddeer.binding.BindingWizard;
import org.jboss.tools.switchyard.reddeer.binding.HTTPBindingPage;
import org.jboss.tools.switchyard.reddeer.component.Component;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.condition.ConsoleHasChanged;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.editor.TextEditor;
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
import org.jboss.tools.switchyard.ui.bot.test.util.HttpClient;
import org.junit.Before;
import org.junit.Test;

/**
 * Creation test from existing Camel route
 * 
 * @author apodhrad
 * 
 */
@CleanWorkspace
@Perspective(name = "Java EE")
@Server(type = Type.ALL, state = State.RUNNING)
public class BottomUpCamelTest extends SWTBotTestCase {

	public static final String PROJECT = "camel_project";
	public static final String PACKAGE = "com.example.switchyard.camel_project";
	public static final String JAVA_FILE = "MyRouteBuilder";
	
	private SWTWorkbenchBot bot = new SWTWorkbenchBot();

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
		new SwitchYardProjectWizard(PROJECT).impl("Camel Route").binding("HTTP").create();
		Project project = new ProjectExplorer().getProject(PROJECT);

		// Import java file
		project.getProjectItem("src/main/java", PACKAGE).select();
		new ImportFileWizard().importFile("resources/java", JAVA_FILE + ".java");

		// Edit java file
		project.getProjectItem("src/main/java", PACKAGE, JAVA_FILE + ".java").open();
		new TextEditor(JAVA_FILE + ".java").deleteLineWith("package")
				.type("package " + PACKAGE + ";").saveAndClose();

		// Add component
		new SwitchYardEditor().addComponent("Component");
		new Component("Component").select();
		new SwitchYardEditor().activateTool("Camel (Java)");
		new Component("Component").click();

		// Select existing implementation
		new PushButton("Browse...").click();
		bot.shell("Select entries").activate();
		new DefaultText(0).setText(JAVA_FILE);
		new WaitUntil(new TableHasRows(new DefaultTable()));
		new PushButton("OK").click();
		bot.shell("Camel Implementation").activate();
		new PushButton("Finish").click();

		// Create new service and interface
		new Component("Component").contextButton("Service").click();
		new NewServiceWizard().createJavaInterface("Hello").activate().finish();

		// Edit the interface
		new Service("Hello").doubleClick();
		new TextEditor("Hello.java").typeAfter("Hello", "String sayHello(String name);")
				.saveAndClose();

		// Edit the camel route
		new Component("Component").doubleClick();
		new TextEditor(JAVA_FILE + ".java").deleteLineWith("file:in")
				.type("from(\"switchyard://Hello\")").deleteLineWith("file:out").type(";")
				.saveAndClose();

		PromoteServiceWizard wizard = new Service("Hello").promoteService();
		wizard.activate().setServiceName("HelloService").finish();

		// Add HTTP binding
		new Service("HelloService").addBinding("HTTP");
		BindingWizard<HTTPBindingPage> httpWizard = BindingWizard.createHTTPBindingWizard();
		httpWizard.getBindingPage().setContextPath(PROJECT);
		httpWizard.getBindingPage().setOperation("sayHello");
		httpWizard.finish();
		
		new SwitchYardEditor().save();

		// Deploy and test the project
		new ServerDeployment().deployProject(PROJECT);
		String url = "http://localhost:8080/" + PROJECT;
		HttpClient httpClient = new HttpClient(url);
		assertEquals("Hello apodhrad", httpClient.send("apodhrad"));
		assertEquals("Hello JBoss", httpClient.send("JBoss"));

		new WaitUntil(new ConsoleHasText("Hello JBoss"));
		new WaitWhile(new ConsoleHasChanged());
	}
}
