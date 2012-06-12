package org.jboss.tools.bpel.ui.bot.test.examples;

import org.jboss.tools.bpel.ui.bot.test.BPELTest;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.junit.Test;

/**
 * 
 * @author apodhrad
 * 
 */
@Require(server = @Server(type = ServerType.JbossAS, state = ServerState.Running, version = "5.1"), perspective = "BPEL")
public class HelloWorldExampleTest extends BPELExampleTest {

	private static final String PROJECT_NAME = "HelloWorld";

	@Override
	public String[] getProjectNames() {
		return new String[] { PROJECT_NAME };
	}

	@Override
	public String getExampleName() {
		return "A simple BPEL example";
	}

	@Override
	protected void executeExample() {
		deployExamples(PROJECT_NAME);
		assertTrue(BPELTest.isProjectDeployed(PROJECT_NAME));

		String url = "http://localhost:8080/bpel/processes/helloWorld";
		String requestFile = "HelloWorld_request.xml";
		String responseFile = "HelloWorld_response.xml";
		testResponse(url, requestFile, responseFile);

		servers.removeAllProjectsFromServer();
	}


}
