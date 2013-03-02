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
@Require(server = @Server(type = ServerType.ALL, state = ServerState.Running), perspective = "BPEL")
public class LoanApprovalExampleTest extends BPELExampleTest {

	private static final String PROJECT_NAME = "Loan_Approval";
	private static final String PROJECT_NAME_WS = "Loan_Approval_WS";
	private static final String WSDL_URL = "http://localhost:8080/Quickstart_bpel_loan_approvalWS?wsdl";

	@Override
	public String[] getProjectNames() {
		return new String[] { PROJECT_NAME_WS, "JSR-109 Web Services" };
	}

	@Override
	public String getExampleName() {
		return "A Web Services Project called by Loan_Approval";
	}

	@Override
	protected void postImport() {
		new LoanApprovalExample().exampleTest();
	}

	@Override
	protected void executeExample() {
		deployExamples(PROJECT_NAME_WS);
		testDeployment(PROJECT_NAME_WS);
		deployExamples(PROJECT_NAME);
		testDeployment(PROJECT_NAME);

		testResponses(WSDL_URL, PROJECT_NAME);

		servers.removeAllProjectsFromServer();
	}

	private class LoanApprovalExample extends BPELExampleTest {

		@Override
		public String[] getProjectNames() {
			return new String[] { PROJECT_NAME };
		}

		@Override
		public String getExampleName() {
			return "A Loan Approval BPEL example";
		}
	}

}
