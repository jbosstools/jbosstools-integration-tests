package org.jboss.tools.bpel.ui.bot.test.examples;

import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

/**
 * 
 * @author apodhrad
 *
 */
@Require(server = @Server(type = ServerType.JbossAS, state = ServerState.Running, version = "5.1"), perspective = "BPEL")
public class HelloWorldOdeExampleTest extends BPELExampleTest {
	
	private static final String PROJECT_NAME = "Hello_World_Header_Ode";

	@Override
	public String[] getProjectNames() {
		return new String[] { PROJECT_NAME };
	}

	@Override
	public String getExampleName() {
		return "A Hello World Header ODE BPEL example";
	}

	@Override
	protected void executeExample() {
		deployExamples(PROJECT_NAME);
		testDeployment(PROJECT_NAME);

		String url = "http://localhost:8080/Quickstart_bpel_hello_world_header_odeWS";
		String requestFile = "Hello_World_Header_Ode_request.xml";
		String responseFile = "Hello_World_Header_Ode_response.xml";
		testResponse(url, requestFile, responseFile);

		servers.removeAllProjectsFromServer();
	}
}
