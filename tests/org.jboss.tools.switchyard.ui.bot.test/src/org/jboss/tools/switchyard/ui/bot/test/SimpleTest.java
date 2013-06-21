package org.jboss.tools.switchyard.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.ProjectItem;
import org.jboss.tools.switchyard.reddeer.component.Bean;
import org.jboss.tools.switchyard.reddeer.component.Component;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.editor.TextEditor;
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

		new Component("ExampleService").doubleClick();
		new TextEditor("ExampleService.java").typeAfter("interface",
				"String sayHello(String name);").saveAndClose();

		new Component("ExampleServiceBean").doubleClick();
		new TextEditor("ExampleServiceBean.java").typeAfter("public class", "@Override").newLine()
				.type("public String sayHello(String name) {").newLine()
				.type("return \"Hello \" + name;}").saveAndClose();
		new SwitchYardEditor().save();

		new Service("ExampleService").newServiceTestClass();
		new SwitchYardEditor().save();

		new TextEditor("ExampleServiceTest.java").deleteLineWith("String message")
				.type("String message=\"Andrej\";").deleteLineWith("assertTrue")
				.type("Assert.assertEquals(\"Hello Andrej\", result);").saveAndClose();
		new SwitchYardEditor().save();

		ProjectItem item = getProject().getProjectItem("src/test/java", PACKAGE,
				"ExampleServiceTest.java");
		new ProjectItemExt(item).runAsJUnitTest();

		assertEquals("1/1", new JUnitView().getRuns());
		assertEquals("0", new JUnitView().getErrors());
		assertEquals("0", new JUnitView().getFailures());

		PromoteServiceWizard wizard = new Service("ExampleService").promoteService();
		wizard.activate().createWSDLInterface("ExampleService.wsdl");
		wizard.activate().next();
		wizard.setTransformerType("Java Transformer").next();
		wizard.setName("ExampleServiceTransformers").finish();

		new ProjectExplorer()
				.getProject(PROJECT)
				.getProjectItem("src/main/java", "com.example.switchyard.test",
						"ExampleServiceTransformers.java").open();
		new TextEditor("ExampleServiceTransformers.java")
				.deleteLineWith("ToSayHello")
				.type("public static String transformStringToSayHelloResponse(String from) {")
				.deleteLineWith("return null")
				.type("return \"<sayHelloResponse xmlns=\\\"urn:com.example.switchyard:test:1.0\\\">"
						+ "<string>\"+ from + \"</string></sayHelloResponse>\";")
				.deleteLineWith("return null").type("return from.getTextContent().trim();")
				.saveAndClose();

		new Service("ExampleServicePortType").addBinding("SOAP");
		new SOAPBindingWizard().setContextpath(PROJECT).finish();

		new SwitchYardEditor().save();

		/* Test SOAP Response */
		try {
			new ServerDeployment("AS-7.1").deployProject(PROJECT);
			SoapClient.testResponses("http://localhost:8080/test/ExampleService?wsdl", "Hello");
		} catch (Exception e) {
			e.printStackTrace();
			new ServerDeployment("AS-7.1").fullPublish(PROJECT);
			SoapClient.testResponses("http://localhost:8080/test/ExampleService?wsdl", "Hello");
		}
	}

	private static Project getProject() {
		return new ProjectExplorer().getProject(PROJECT);
	}

}
