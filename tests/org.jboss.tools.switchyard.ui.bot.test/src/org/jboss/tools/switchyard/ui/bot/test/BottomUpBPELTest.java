package org.jboss.tools.switchyard.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.switchyard.reddeer.binding.BindingWizard;
import org.jboss.tools.switchyard.reddeer.binding.SOAPBindingPage;
import org.jboss.tools.switchyard.reddeer.component.Component;
import org.jboss.tools.switchyard.reddeer.component.Service;
import org.jboss.tools.switchyard.reddeer.condition.ConsoleHasChanged;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.wizard.ImportFileWizard;
import org.jboss.tools.switchyard.reddeer.wizard.PromoteServiceWizard;
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
 * Creation test from existing BPEL process
 * 
 * @author apodhrad
 * 
 */
@CleanWorkspace
@Perspective(name = "Java EE")
@Server(type = Type.ALL, state = State.RUNNING)
public class BottomUpBPELTest extends SWTBotTestCase {

	public static final String PROJECT = "bpel_project";
	public static final String WSDL = "http://localhost:8080/bpel_project/SayHelloService?wsdl";

	@Before
	public void closeSwitchyardFile() {
		try {
			new SwitchYardEditor().saveAndClose();
		} catch (Exception ex) {
			// it is ok, we just try to close switchyard.xml if it is open
		}
	}

	@Test
	public void bottomUpBPELtest() throws Exception {
		new SwitchYardProjectWizard(PROJECT).impl("BPEL").binding("SOAP").create();
		new ProjectExplorer().getProject(PROJECT).getProjectItem("src/main/resources").select();
		new ImportFileWizard().importFile("resources/bpel", "SayHello.bpel");
		new ImportFileWizard().importFile("resources/wsdl", "SayHelloArtifacts.wsdl");

		// There is no way how to create deployment descriptor, only manually
		new ImportFileWizard().importFile("resources/bpel", "deploy.xml");

		new SwitchYardEditor().addComponent("Component");
		new Component("Component").select();
		new SwitchYardEditor().activateTool("Process (BPEL)");
		new Component("Component").click();

		new PushButton("Browse...").click();
		new DefaultText(0).setText("SayHello.bpel");
		new PushButton("OK").click();
		new PushButton("Finish").click();

		new Component("Component").contextButton("Service").click();
		new PushButton("Browse...").click();
		new DefaultText(0).setText("SayHelloArtifacts.wsdl");
		new PushButton("OK").click();
		new PushButton("Finish").click();

		PromoteServiceWizard wizard = new Service("SayHello").promoteService();
		wizard.activate().setServiceName("SayHelloService").finish();

		new Service("SayHelloService").addBinding("SOAP");
		BindingWizard<SOAPBindingPage> soapWizard = BindingWizard.createSOAPBindingWizard();
		soapWizard.getBindingPage().setContextPath(PROJECT);
		soapWizard.finish();
		
		new SwitchYardEditor().save();

		/* Test SOAP Response */
		new ServerDeployment().deployProject(PROJECT);
		new ServerDeployment().fullPublish(PROJECT);
		try {
			SoapClient.testResponses(WSDL, "SayHello");
		} catch (Exception e) {
			BackupClient.backupDeployment(PROJECT);
			throw e;
		}

		new WaitWhile(new ConsoleHasChanged());
	}
}
