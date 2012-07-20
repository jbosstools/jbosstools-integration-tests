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
public class SimpleInvokeExampleTest extends BPELExampleTest {

	private static final String PROJECT_NAME = "Simple_Invoke";
	private static final String PROJECT_NAME_WS = "Simple_Invoke_WS";
	private static final String WSDL_URL = "http://localhost:8080/Quickstart_bpel_simple_invokeWS?wsdl";

	@Override
	public String[] getProjectNames() {
		return new String[] { PROJECT_NAME_WS };
	}

	@Override
	public String getExampleName() {
		return "A Web Service project called by Simple Invoke BPEL example";
	}

	@Override
	protected void postImport() {
		new SimpleInvokeExample().exampleTest();
	}

	@Override
	protected void executeExample() {
		deployExamples(PROJECT_NAME_WS);
		assertTrue(BPELTest.isProjectDeployed(PROJECT_NAME_WS));
		deployExamples(PROJECT_NAME);
		assertTrue(BPELTest.isProjectDeployed(PROJECT_NAME));

		testResponses(WSDL_URL, PROJECT_NAME);

		servers.removeAllProjectsFromServer();
	}

	private class SimpleInvokeExample extends BPELExampleTest {

		@Override
		public String[] getProjectNames() {
			return new String[] { PROJECT_NAME };
		}

		@Override
		public String getExampleName() {
			return "A Simple Invoke BPEL example";
		}
	}

}
