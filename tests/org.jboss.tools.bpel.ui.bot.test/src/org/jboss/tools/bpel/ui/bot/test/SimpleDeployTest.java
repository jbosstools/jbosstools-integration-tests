package org.jboss.tools.bpel.ui.bot.test;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.tools.bpel.reddeer.activity.Assign;
import org.jboss.tools.bpel.reddeer.activity.Empty;
import org.jboss.tools.bpel.reddeer.activity.Receive;
import org.jboss.tools.bpel.reddeer.activity.Reply;
import org.jboss.tools.bpel.reddeer.activity.Sequence;
import org.jboss.tools.bpel.reddeer.editor.BpelDescriptorEditor;
import org.jboss.tools.bpel.reddeer.server.ServerDeployment;
import org.jboss.tools.bpel.reddeer.wizard.NewDescriptorWizard;
import org.jboss.tools.bpel.reddeer.wizard.NewProcessWizard;
import org.jboss.tools.bpel.reddeer.wizard.NewProjectWizard;
import org.jboss.tools.bpel.ui.bot.test.suite.BPELSuite;
import org.jboss.tools.bpel.ui.bot.test.suite.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.bpel.ui.bot.test.suite.PerspectiveRequirement.Perspective;
import org.jboss.tools.bpel.ui.bot.test.suite.ServerRequirement.Server;
import org.jboss.tools.bpel.ui.bot.test.suite.ServerRequirement.State;
import org.jboss.tools.bpel.ui.bot.test.suite.ServerRequirement.Type;
import org.jboss.tools.bpel.ui.bot.test.util.SoapClient;
import org.junit.Test;

/**
 * 
 * @author apodhrad
 * 
 */
@CleanWorkspace
@Perspective(name = "BPEL")
@Server(type = Type.ALL, state = State.RUNNING)
public class SimpleDeployTest extends SWTBotTestCase {

	private static final String WSDL_URL = "http://localhost:8080/deployHello?wsdl";

	@Test
	public void simpleDeployTest() throws Exception {
		new WorkbenchShell().maximize();
		
		String projectName = "deployTest";
		String processName = "deployHello";

		new NewProjectWizard(projectName).execute();
		new NewProcessWizard(projectName, processName).setSyncTemplate().execute();

		// call validate when implemented
		new Sequence("main");
		new Receive("receiveInput");
		new Empty("FIX_ME-Add_Business_Logic_Here");
		new Reply("replyOutput");

		// change Empty to Assign
		Assign assign = new Empty("FIX_ME-Add_Business_Logic_Here").toAssign();
		assign.setName("addHello");
		assign.addExpToVar("concat('Hello ', $input.payload/tns:input)", new String[] {
				"output : deployHelloResponseMessage", "payload : deployHelloResponse", "result : string" });

		// create descriptor
		new NewDescriptorWizard(projectName).execute();
		new BpelDescriptorEditor().setAssociatedPort("deployHelloPort");

		// deploy project
		String serverName = BPELSuite.getServerName();
		ServerDeployment server = new ServerDeployment(serverName);
		server.deployProject(projectName);
		AbstractWait.sleep(5 * 1000);

		// test the deployed project
		SoapClient.testResponses(WSDL_URL, "Deploy_Hello");
	}

}
