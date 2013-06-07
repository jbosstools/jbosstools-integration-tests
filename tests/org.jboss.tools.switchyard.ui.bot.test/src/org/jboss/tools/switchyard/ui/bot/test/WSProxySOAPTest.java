package org.jboss.tools.switchyard.ui.bot.test;

import java.util.List;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.eclipse.jdt.ui.NewJavaClassWizardDialog;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.switchyard.reddeer.component.Component;
import org.jboss.tools.switchyard.reddeer.component.Reference;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.server.ServerDeployment;
import org.jboss.tools.switchyard.reddeer.wizard.CamelJavaWizard;
import org.jboss.tools.switchyard.reddeer.wizard.ImportFileWizard;
import org.jboss.tools.switchyard.reddeer.wizard.ReferenceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.SOAPBindingWizard;
import org.jboss.tools.switchyard.reddeer.wizard.ServiceWizard;
import org.jboss.tools.switchyard.reddeer.wizard.SwitchYardProjectWizard;
import org.jboss.tools.switchyard.ui.bot.test.suite.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.switchyard.ui.bot.test.suite.PerspectiveRequirement.Perspective;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.Server;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.State;
import org.jboss.tools.switchyard.ui.bot.test.suite.ServerRequirement.Type;
import org.jboss.tools.switchyard.ui.bot.test.util.SoapClient;
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

	public static final String PROJECT = "proxy";
	private static final String WSDL = "Hello.wsdl";

	@Test
	public void wsProxySoapTest() {
		try {
			new SwitchYardEditor().saveAndClose();
		} catch(Exception ex) {
			// it is ok, we just try to close switchyard.xml if it is open
		}
		
		/* Create Web Service */
		new WebProjectWizard("web").create();
		new ProjectExplorer().getProject("web").select();
		new WebServiceWizard("Hello").create();
		new ServerDeployment("AS-7.1").deployProject("web");

		/* Create SwicthYard Project */
		new SwitchYardProjectWizard(PROJECT).impl("Camel Route").binding("SOAP").create();
		new ProjectExplorer().getProject(PROJECT).getProjectItem("src/main/resources").select();
		new ImportFileWizard().importFile("resources/wsdl", WSDL);
		new CamelJavaWizard().open().setName("Proxy").selectWSDLInterface(WSDL).finish();

		new Service("Hello").promoteService();
		new ServiceWizard().setServiceName("ProxyService").finish();
		new Service("ProxyService").addBinding("SOAP");
		new SOAPBindingWizard().setContextpath(PROJECT).finish();
		new Component(PROJECT).contextButton("Reference").click();
		new ReferenceWizard().selectWSDLInterface(WSDL).setServiceName("HelloService").finish();
		new Reference("HelloService").promoteReference();
		new ServiceWizard().setServiceName("HelloService").finish();
		new Service("HelloService").addBinding("SOAP").finish();
		new SwitchYardEditor().save();

		editCamelRoute();

		/* Deploy Project */
		new ServerDeployment("AS-7.1").deployProject(PROJECT);
		ConsoleView consoleView = new ConsoleView();
		consoleView.open();
		String consoleText = consoleView.getConsoleText().toUpperCase();
		assertFalse("Deployment error!", consoleText.contains("ERROR"));

		/* Test Web Service Proxy */
		SoapClient.testResponses("http://localhost:8080/proxy/HelloService?wsdl", "WSProxy");
	}

	private void editCamelRoute() {
		new Component("Proxy").doubleClick();
		Bot.get().sleep(1000);

		SWTBotEclipseEditor editor = Bot.get().editorByTitle("Proxy.java").toTextEditor();
		int lineNum = 0;
		List<String> lines = editor.getLines();
		for (String line : lines) {
			if (line.contains("log(")) {
				break;
			}
			lineNum++;
		}
		editor.typeText(lineNum, lines.get(lineNum).length() - 1,
				".to(\"switchyard://HelloService\")");
		editor.saveAndClose();
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

			SWTBotEclipseEditor editor = Bot.get().editorByTitle(name + ".java").toTextEditor();
			int lineNum = 0;
			List<String> lines = editor.getLines();
			for (String line : lines) {
				if (line.contains(name)) {
					break;
				}
				lineNum++;
			}
			editor.typeText(lineNum, 0, "import javax.jws.*;\n\n"
					+ "@WebService(targetNamespace = \"urn:webservice:" + name + ":1.0\")\n");
			editor.typeText(lineNum + 5, 0, "\t@WebMethod\n" + "@WebResult(name=\"text\")\n"
					+ "public String sayHello(@WebParam(name=\"name\") String name) {\n"
					+ "return \"Hello \" + name;\n\n");
			editor.saveAndClose();
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
