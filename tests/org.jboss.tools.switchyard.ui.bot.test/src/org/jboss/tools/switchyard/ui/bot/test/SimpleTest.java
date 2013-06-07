package org.jboss.tools.switchyard.ui.bot.test;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.eclipse.swtbot.swt.finder.keyboard.Keystrokes;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.ProjectItem;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.switchyard.reddeer.component.Bean;
import org.jboss.tools.switchyard.reddeer.component.Component;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.server.ServerDeployment;
import org.jboss.tools.switchyard.reddeer.view.JUnitView;
import org.jboss.tools.switchyard.reddeer.widget.ProjectItemExt;
import org.jboss.tools.switchyard.reddeer.wizard.PromoteServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.SOAPBindingWizard;
import org.jboss.tools.switchyard.reddeer.wizard.SwitchYardProjectWizard;
import org.jboss.tools.switchyard.ui.bot.test.suite.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.switchyard.ui.bot.test.suite.PerspectiveRequirement.Perspective;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.Server;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.State;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.Type;
import org.jboss.tools.switchyard.ui.bot.test.util.SoapClient;
import org.junit.Test;

/**
 * This simple switchyard test performs the following tasks:
 * 
 * <ol>
 * <li>Create a SwitchYard project
 * <li>Create a Java service interface
 * <li>Create a bean component implementation
 * <li>Create and execute a unit test for the service
 * <li>Create a WSDL service interface
 * <li>Add a SOAP gateway for accessing the service
 * <li>Create a transformer
 * <li>Create and execute a unit test for the transformer
 * <li>Create and execute a unit test for the SOAP gateway
 * <li>Deploy the application to a server
 * <li>Test the deployed application
 * </ol>
 * 
 * @author apodhrad
 * 
 */
@CleanWorkspace
@Perspective(name = "Java EE")
@Server(type = Type.ALL, state = State.RUNNING)
public class SimpleTest extends SWTBotTestCase {

	private static final String PROJECT = "test";
	private static final String PACKAGE = "com.example.switchyard.test";

	@Test
	public void simpleTest() throws Exception {
		new SwitchYardProjectWizard(PROJECT).impl("Bean").binding("SOAP").create();

		new Bean().setService("ExampleService").create();

		SWTBotEclipseEditor editor = null;

		editor = openJavaFileFromService("ExampleService");
		editor.typeText(3, 0, "\tString sayHello(String name);");
		editor.saveAndClose();

		editor = openJavaFileFromService("ExampleServiceBean");
		editor.typeText(6, 0, "\t@Override\npublic String sayHello(String name) {\n");
		editor.typeText(8, 0, "\treturn \"Hello \" + name;");
		editor.saveAndClose();

		new Service("ExampleService").newServiceTestClass();

		editor = Bot.get().editorByTitle("ExampleServiceTest.java").toTextEditor();
		editor.selectRange(15, 19, 4);
		editor.pressShortcut(Keystrokes.DELETE);
		editor.typeText(15, 19, "\"Andrej\"");
		editor.selectLine(20);
		editor.pressShortcut(Keystrokes.DELETE);
		editor.typeText(20, 0, "\tAssert.assertEquals(\"Hello Andrej\", result);");
		editor.saveAndClose();

		new SwitchYardEditor().save();
		
		ProjectItem item = getProject().getProjectItem("src/test/java", PACKAGE, "ExampleServiceTest.java");
		new ProjectItemExt(item).runAsJUnitTest();

		assertEquals("1/1", new JUnitView().getRuns());
		assertEquals("0", new JUnitView().getErrors());
		assertEquals("0", new JUnitView().getFailures());

		new Service("ExampleService").promoteService();

		new PromoteServiceWizard().setInterfaceType("WSDL").setTransformerName("ExampleServiceTransformers")
				.create();

		editor = openJavaFileFromExplorer(PACKAGE, "ExampleServiceTransformers.java");
		editor.selectLine(7);
		editor.pressShortcut(Keystrokes.DELETE);
		editor.typeText(7, 0, "\tpublic static String transformStringToSayHelloResponse(String from) {");
		editor.selectLine(9);
		editor.pressShortcut(Keystrokes.DELETE);
		editor.typeText(9, 0,
				"\treturn \"<sayHelloResponse xmlns=\\\"urn:com.example.switchyard:test:1.0\\\">"
						+ "<string>\"+ from + \"</string></sayHelloResponse>\";");
		editor.selectLine(15);
		editor.pressShortcut(Keystrokes.DELETE);
		editor.typeText(15, 0, "\treturn from.getTextContent().trim();");
		editor.saveAndClose();

		new Service("ExampleServicePortType").addBinding("SOAP");
		new SOAPBindingWizard().setContextpath(PROJECT).finish();
		
		new SwitchYardEditor().save();

		/* Deploy Project*/
		new ServerDeployment("AS-7.1").deployProject(PROJECT);
		ConsoleView consoleView = new ConsoleView();
		consoleView.open();
		String consoleText = consoleView.getConsoleText().toUpperCase();
		assertFalse("Deployment error!", consoleText.contains("ERROR"));
		
		/* Test SOAP Response */
		SoapClient.testResponses("http://localhost:8080/test/ExampleService?wsdl", "Hello");
	}

	private SWTBotEclipseEditor openJavaFileFromService(String component) {
		new Component(component).doubleClick();
		Bot.get().sleep(1000);
		return Bot.get().editorByTitle(component + ".java").toTextEditor();
	}

	private SWTBotEclipseEditor openJavaFileFromExplorer(String pgk, String file) {
		new SwitchYardEditor().save();
		new ProjectExplorer().getProject(PROJECT).getProjectItem("src/main/java", pgk, file).open();
		return Bot.get().editorByTitle(file).toTextEditor();
	}

	private static Project getProject() {
		return new ProjectExplorer().getProject(PROJECT);
	}

}
