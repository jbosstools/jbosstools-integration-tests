package org.jboss.tools.bpel.ui.bot.test.examples;

import org.jboss.tools.bpel.ui.bot.test.BPELTest;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;

/**
 * 
 * @author apodhrad
 * 
 */
@Require(server = @Server(type = ServerType.ALL, state = ServerState.Running), perspective = "BPEL")
public class MathExampleTest extends BPELExampleTest {

	private static final String PROJECT_NAME = "Math";

	@Override
	public String[] getProjectNames() {
		return new String[] { PROJECT_NAME };
	}

	@Override
	public String getExampleName() {
		return "A Math BPEL example";
	}

	@Override
	protected void executeExample() {
		deployExamples(PROJECT_NAME);
		assertTrue(BPELTest.isProjectDeployed(PROJECT_NAME));

		String url = "http://localhost:8080/MathProcess?wsdl";
		String requestFile = "Math_request.xml";
		String responseFile = "Math_response.xml";
		testResponse(url, requestFile, responseFile);

		servers.removeAllProjectsFromServer();
	}


}
