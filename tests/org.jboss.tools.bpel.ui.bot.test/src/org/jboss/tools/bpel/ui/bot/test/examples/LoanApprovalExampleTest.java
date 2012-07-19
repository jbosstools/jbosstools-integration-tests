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
public class LoanApprovalExampleTest extends BPELExampleTest {

	private static final String PROJECT_NAME = "Loan_Approval";
	private static final String PROJECT_NAME_WS = "Loan_Approval_WS";

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
		assertTrue(BPELTest.isProjectDeployed(PROJECT_NAME_WS));
		deployExamples(PROJECT_NAME);
		assertTrue(BPELTest.isProjectDeployed(PROJECT_NAME));

		String url = "http://localhost:8080/Quickstart_bpel_loan_approvalWS?wsdl";
		String requestFile = "Loan_Approval_request_1.xml";
		String responseFile = "Loan_Approval_response_1.xml";
		testResponse(url, requestFile, responseFile);
		requestFile = "Loan_Approval_request_2.xml";
		responseFile = "Loan_Approval_response_2.xml";
		testResponse(url, requestFile, responseFile);
		requestFile = "Loan_Approval_request_3.xml";
		responseFile = "Loan_Approval_response_3.xml";
		// get http response 500, why?
		// testResponse(url, requestFile, responseFile);

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
