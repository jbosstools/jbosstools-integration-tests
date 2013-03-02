package org.jboss.tools.bpel.ui.bot.test;

import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.bpel.ui.bot.ext.activity.Assign;
import org.jboss.tools.bpel.ui.bot.ext.activity.Empty;
import org.jboss.tools.bpel.ui.bot.ext.activity.Receive;
import org.jboss.tools.bpel.ui.bot.ext.activity.Reply;
import org.jboss.tools.bpel.ui.bot.ext.activity.Sequence;
import org.jboss.tools.bpel.ui.bot.ext.editor.BpelDescriptorEditor;
import org.jboss.tools.bpel.ui.bot.ext.util.SoapClient;
import org.jboss.tools.bpel.ui.bot.ext.wizard.NewDescriptorWizard;
import org.jboss.tools.bpel.ui.bot.ext.wizard.NewProcessWizard;
import org.jboss.tools.bpel.ui.bot.ext.wizard.NewProjectWizard;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.view.ServersView;
import org.junit.Test;

/**
 * 
 * @author apodhrad
 * 
 */
@Require(server = @Server(type = ServerType.ALL, state = ServerState.Running), perspective = "BPEL")
public class SimpleDeployTest extends SWTTestExt {

	private static final String WSDL_URL = "http://localhost:8080/deployHello?wsdl";

	@Test
	public void simpleDeployTest() throws Exception {
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
				"output : deployHelloResponseMessage", "payload : deployHelloResponse",
				"result : string" });

		// create descriptor
		new NewDescriptorWizard(projectName).execute();
		new BpelDescriptorEditor().setAssociatedPort("deployHelloPort");

		// deploy project
		String serverName = configuredState.getServer().name;
		new ServersView().addProjectToServer(projectName, serverName);
		Bot.get().sleep(TIME_5S);

		// test the deployed project
		SoapClient.testResponses(WSDL_URL, "Deploy_Hello");
	}

}
