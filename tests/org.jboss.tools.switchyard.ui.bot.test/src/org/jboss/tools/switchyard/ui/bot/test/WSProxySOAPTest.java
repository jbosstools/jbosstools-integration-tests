package org.jboss.tools.switchyard.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.switchyard.reddeer.binding.BindingWizard;
import org.jboss.tools.switchyard.reddeer.binding.SOAPBindingPage;
import org.jboss.tools.switchyard.reddeer.component.Component;
import org.jboss.tools.switchyard.reddeer.component.Reference;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.condition.ConsoleHasChanged;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.editor.TextEditor;
import org.jboss.tools.switchyard.reddeer.wizard.CamelJavaWizard;
import org.jboss.tools.switchyard.reddeer.wizard.ImportFileWizard;
import org.jboss.tools.switchyard.reddeer.wizard.ReferenceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.SwitchYardProjectWizard;
import org.jboss.tools.switchyard.ui.bot.test.suite.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.switchyard.ui.bot.test.suite.PerspectiveRequirement.Perspective;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerDeployment;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.Server;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.State;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.Type;
import org.jboss.tools.switchyard.ui.bot.test.util.BackupClient;
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
public class WSProxySOAPTest extends SWTBotTestCase {

	public static final String PROJECT = "proxy_soap";
	private static final String WSDL = "Hello.wsdl";

	@Before
	public void closeSwitchyardFile() {
		try {
			new SwitchYardEditor().saveAndClose();
		} catch (Exception ex) {
			// it is ok, we just try to close switchyard.xml if it is open
		}
	}

	@Test
	public void wsProxySoapTest() throws Exception {
		/* Create Web Service */
		new WebProjectWizard("web").create();
		new ProjectExplorer().getProject("web").select();
		new WebServiceWizard("Hello").create();
		new ServerDeployment().deployProject("web", "web.war");

		/* Create SwicthYard Project */
		new SwitchYardProjectWizard(PROJECT).impl("Camel Route").binding("SOAP").create();
		new ProjectExplorer().getProject(PROJECT).getProjectItem("src/main/resources").select();
		new ImportFileWizard().importFile("resources/wsdl", WSDL);
		new CamelJavaWizard().open().setName("Proxy").selectWSDLInterface(WSDL).finish();

		new Service("Hello").promoteService().setServiceName("ProxyService").finish();
		new Service("ProxyService").addBinding("SOAP");
		BindingWizard<SOAPBindingPage> soapWizard = BindingWizard.createSOAPBindingWizard();
		soapWizard.getBindingPage().setContextPath(PROJECT);
		soapWizard.finish();
		new Component(PROJECT).contextButton("Reference").click();
		new ReferenceWizard().selectWSDLInterface(WSDL).setServiceName("HelloService").finish();
		new Reference("HelloService").promoteReference().setServiceName("HelloService").finish();
		new Service("HelloService").addBinding("SOAP").finish();
		new SwitchYardEditor().save();

		/* Edit Camel Route */
		new Component("Proxy").doubleClick();
		new TextEditor("Proxy.java").typeAfter("from(", ".to(\"switchyard://HelloService\")")
				.saveAndClose();
		new SwitchYardEditor().save();

		/* Test Web Service Proxy */

		/* Test SOAP Response */
		new ServerDeployment().deployProject(PROJECT);
		new ServerDeployment().fullPublish(PROJECT);
		try {
			SoapClient.testResponses("http://localhost:8080/" + PROJECT + "/HelloService?wsdl",
					"WSProxy");
		} catch (Exception e) {
			BackupClient.backupDeployment(PROJECT);
			throw e;
		}

		new WaitWhile(new ConsoleHasChanged());
	}

	private class WebServiceWizard extends NewJavaClassWizardDialog {

		public static final String DEFAULT_PACKAGE = "com.example.webservice";

		private String name;

		public WebServiceWizard(String name) {
			super();
			this.name = name;
		}

		public void create() {
			open();
			getFirstPage().setName(name);
			getFirstPage().setPackage(DEFAULT_PACKAGE);
			finish();

			new TextEditor(name + ".java").typeAfter("package", "import javax.jws.*;").newLine()
					.type("@WebService(targetNamespace = \"urn:webservice:" + name + ":1.0\")")
					.typeAfter("class", "@WebMethod").newLine()
					.type("@WebResult(name=\"greeting\")").newLine()
					.type("public String sayHello(@WebParam(name=\"name\") String name) {")
					.newLine().type("return \"Hello \" + name;}").saveAndClose();
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
			new LabeledText("Project name:").setText(projectName);
			finish();
		}
	}
}
