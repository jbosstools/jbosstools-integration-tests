package org.jboss.tools.switchyard.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.switchyard.reddeer.component.Component;
import org.jboss.tools.switchyard.reddeer.component.Reference;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.editor.TextEditor;
import org.jboss.tools.switchyard.reddeer.server.ServerDeployment;
import org.jboss.tools.switchyard.reddeer.wizard.CamelJavaWizard;
import org.jboss.tools.switchyard.reddeer.wizard.PromoteServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.RESTBindingWizard;
import org.jboss.tools.switchyard.reddeer.wizard.ReferenceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.SOAPBindingWizard;
import org.jboss.tools.switchyard.reddeer.wizard.SwitchYardProjectWizard;
import org.jboss.tools.switchyard.ui.bot.test.suite.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.switchyard.ui.bot.test.suite.PerspectiveRequirement.Perspective;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.Server;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.State;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.Type;
import org.jboss.tools.switchyard.ui.bot.test.suite.SwitchyardSuite;
import org.jboss.tools.switchyard.ui.bot.test.util.SoapClient;
import org.junit.Before;
import org.junit.Test;

/**
 * Web Service Proxy Test
 * 
 * @author apodhrad
 * 
 */
@CleanWorkspace
@Perspective(name = "Java EE")
@Server(type = Type.ALL, state = State.RUNNING)
public class WSProxyRESTTest extends SWTBotTestCase {

	public static final String REST_URL = "http://localhost:8080/rest/MyRESTApplication";
	public static final String REST_SERVICE = "HelloRESTService";
	public static final String PROJECT = "proxy-rest";
	public static final String PACKAGE = "com.example.switchyard.proxy_rest";

	@Before
	public void closeSwitchyardFile() {
		try {
			new SwitchYardEditor().saveAndClose();
		} catch (Exception ex) {
			// it is ok, we just try to close switchyard.xml if it is open
		}
	}

	@Test
	public void wsProxySoapTest() {
		/* Create RESTful Service */
		new WebProjectWizard("rest").create();
		new ProjectExplorer().getProject("rest").select();
		new RestServiceWizard().create();
		new TextEditor("HelloWorldResource.java").typeAfter("@GET()", "@Path(\"/{name}\")")
				.newLine().deleteLineWith("sayHello").deleteLineWith("return")
				.type("public String sayHello(@PathParam(\"name\") String name) {").newLine()
				.type("return \"Hello \" + name;").saveAndClose();

		/* Deploy RESTful Service */
		new ServerDeployment(SwitchyardSuite.getServerName()).deployProject("rest");

		/* Create SwicthYard Project */
		new SwitchYardProjectWizard(PROJECT).impl("Camel Route").binding("SOAP", "REST").create();
		new CamelJavaWizard().open().setName("Proxy").createJavaInterface("Hello").finish();

		/* Edit Hello interface */
		new Service("Hello").doubleClick();

		new TextEditor("Hello.java").typeBefore("Hello", "import javax.ws.rs.Produces;").newLine()
				.type("import javax.ws.rs.GET;").newLine().type("import javax.ws.rs.Path;")
				.newLine().type("import javax.ws.rs.PathParam;").newLine()
				.typeAfter("Hello", "@GET()").newLine().type("@Path(\"/{name}\")").newLine()
				.type("@Produces(\"text/plain\")").newLine()
				.type("String sayHello(@PathParam(\"name\") String name);").saveAndClose();
		new SwitchYardEditor().save();

		PromoteServiceWizard wizard = new Service("Hello").promoteService();
		wizard.activate().createWSDLInterface("Hello.wsdl");
		wizard.activate().next();
		wizard.setTransformerType("Java Transformer").next();
		wizard.setName("HelloTransformer").finish();
		new SwitchYardEditor().save();

		/* Edit HelloTransformer */
		new ProjectExplorer().getProject(PROJECT)
				.getProjectItem("src/main/java", PACKAGE, "HelloTransformer.java").open();
		new TextEditor("HelloTransformer.java")
				.deleteLineWith("ToSayHello")
				.type("public static String transformStringToSayHelloResponse(String from) {")
				.deleteLineWith("return null")
				.type("return \"<sayHelloResponse xmlns=\\\"urn:com.example.switchyard:proxy-rest:1.0\\\">"
						+ "<string>\"+ from + \"</string></sayHelloResponse>\";")
				.deleteLineWith("return null").type("return from.getTextContent().trim();")
				.saveAndClose();
		new SwitchYardEditor().save();

		/* Expose Proxy Service Through SOAP */
		new Service("HelloPortType").addBinding("SOAP");
		new SOAPBindingWizard().activate().setContextpath(PROJECT).finish();
		new SwitchYardEditor().save();

		/* Reference to RESTful Service */
		new Component("Proxy").contextButton("Reference").click();
		new ReferenceWizard().selectJavaInterface("Hello").setServiceName(REST_SERVICE).finish();
		new Reference(REST_SERVICE).promoteReference().finish();
		new Service(REST_SERVICE).addBinding("REST");
		new RESTBindingWizard().setAddress(REST_URL).addInterface("Hello").finish();

		/* Edit Camel Route */
		new Component("Proxy").doubleClick();
		new TextEditor("Proxy.java").typeAfter("from(",
				".to(\"switchyard://" + REST_SERVICE + "\")").saveAndClose();
		new SwitchYardEditor().save();

		/* Test Web Service Proxy */
		new ServerDeployment(SwitchyardSuite.getServerName()).deployProject(PROJECT);
		SoapClient.testResponses("http://localhost:8080/" + PROJECT + "/Hello?wsdl", "WSProxyREST");
		Bot.get().sleep(10 * 1000);
	}

	private class RestServiceWizard extends NewWizardDialog {

		public RestServiceWizard() {
			super("Web Services", "Create a Sample RESTful Web Service");
		}

		public void create() {
			open();
			// Bot.get().shell("Generate a Sample RESTful Web Service").activate();
			finish();
		}
	}

	private class WebProjectWizard extends NewWizardDialog {

		private String projectName;

		public WebProjectWizard(String projectName) {
			super("Web", "Dynamic Web Project");
			this.projectName = projectName;
		}

		public void create() {
			open();
			// Bot.get().shell("New Dynamic Web Project").activate();
			new LabeledText("Project name:").setText(projectName);
			finish();
		}
	}
}
