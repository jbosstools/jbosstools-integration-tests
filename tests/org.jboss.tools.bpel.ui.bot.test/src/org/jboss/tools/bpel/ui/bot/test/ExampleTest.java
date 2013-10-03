package org.jboss.tools.bpel.ui.bot.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.soap.SOAPException;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.eclipse.swtbot.swt.finder.exceptions.AssertionFailedException;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.tools.bpel.reddeer.server.ServerDeployment;
import org.jboss.tools.bpel.reddeer.wizard.ExampleWizard;
import org.jboss.tools.bpel.ui.bot.test.suite.BPELSuite;
import org.jboss.tools.bpel.ui.bot.test.suite.CleanWorkspaceRequirement.CleanWorkspace;
import org.jboss.tools.bpel.ui.bot.test.suite.PerspectiveRequirement.Perspective;
import org.jboss.tools.bpel.ui.bot.test.suite.ServerRequirement.Server;
import org.jboss.tools.bpel.ui.bot.test.suite.ServerRequirement.State;
import org.jboss.tools.bpel.ui.bot.test.suite.ServerRequirement.Type;
import org.jboss.tools.bpel.ui.bot.test.util.ResourceHelper;
import org.jboss.tools.bpel.ui.bot.test.util.SoapClient;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * Test BPEL examples
 * 
 * @author apodhrad
 *
 */
@CleanWorkspace
@Perspective(name = "BPEL")
@Server(type = Type.ALL, state = State.RUNNING)
public class ExampleTest extends SWTBotTestCase {

	public static final String HOST_URL = "http://localhost:8080";
	public static final String EXAMPLE_CATEGORY = "BPEL";
	public static final String MESSAGE_DIR = "messages";

	private List<BpelExample> bpelExamples;

	public ExampleTest() {
		add("A simple BPEL example", "HelloWorld", "bpel/processes/helloWorld");
		add("Hello_World_Header_Ode", "Hello_World_Header_Ode", "Quickstart_bpel_hello_world_header_odeWS");
		add("A Hello World Header WSDL BPEL example", "Hello_World_Header_WSDL", "Quickstart_bpel_hello_world_header_wsdlWS");
		add("A Math BPEL example", "Math", "MathProcess");
		add("A Salutations BPEL example", "Salutations", "SalutationsProcess");
		add("A Say Hello BPEL example", "Say_Hello", "SayHelloProcess");
		add("A Service Handler BPEL example", "Service_Handler", "Quickstart_bpel_service_handlerWS");
		add("A Simple Pick BPEL example", "Simple_Pick", "Quickstart_bpel_simple_pickWS");
		add("A correlation BPEL example", "Simple_Correlation", "Quickstart_bpel_simple_correlationWS");
		add("A Fault Compensation BPEL example", "Fault_Compensation", "Quickstart_bpel_fault_compensationWS");
		add("A Synchronous Web Service Interactions BPEL example", "BluePrint1", "BPEL_BluePrint1_PurchaseOrderService");
		add("A Asynchronous Web Service Interactions BPEL example", "BluePrint2", "BPEL_BluePrint2_PurchaseOrderService");
		add("A Fault Handling BPEL example", "BluePrint3", "BPEL_BluePrint3_PurchaseOrderService");
		add("A Message-Based Coordination of Events BPEL example", "BluePrint4", "BPEL_BluePrint4_PurchaseOrderService");
		add("A Concurrent Asynchronous Coordination Events BPEL example", "BluePrint5", "BPEL_BluePrint5_ReservationService");
	}

	private void add(String example, String project, String wsdl) {
		if (bpelExamples == null) {
			bpelExamples = new ArrayList<ExampleTest.BpelExample>();
		}
		bpelExamples.add(new BpelExample(example, project, wsdl));
	}

	@Test
	public void testExamples() throws Exception {
		for (BpelExample bpelExample : bpelExamples) {
			bpelExample.test();
		}
	}

	/**
	 * A simple BPEL example
	 * 
	 * @author apodhrad
	 *
	 */
	private class BpelExample {

		private String exampleName;
		private String projectName;
		private String wsdl;

		public BpelExample(String exampleName, String projectName, String wsdl) {
			this.exampleName = exampleName;
			this.projectName = projectName;
			this.wsdl = wsdl;
		}

		public void test() {
			new ExampleWizard(EXAMPLE_CATEGORY, exampleName).execute();
			deployToServer();
			testResponses();
		}

		public void deployToServer() {
			String serverName = BPELSuite.getServerName();
			ServerDeployment server = new ServerDeployment(serverName);
			server.deployProject(projectName);
			AbstractWait.sleep(5 * 1000);
		}

		public void testResponses() {
			String url = HOST_URL + "/" + wsdl;
			if (!url.endsWith("?wsdl")) {
				url += "?wsdl";
			}
			String dir = projectName;
			try {
				List<String> requests = getRequestMessages(dir);
				for (String request : requests) {
					request = dir + "/" + request;
					String response = request.replace("request", "response");
					testResponse(url, request, response);
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new AssertionFailedException("IOException during testing response.");
			}
		}

		private void testResponse(String url, String requestFile, String responseFile) {
			XMLUnit.setIgnoreWhitespace(true);
			try {
				String requestMessage = getMessageFromFile(requestFile);
				String responseMessage = getMessageFromFile(responseFile);
				String response = SoapClient.sendMessage(url, requestMessage);
				Diff diff = new Diff(response, responseMessage);
				assertTrue("Expected response is\n" + responseMessage + "\nbut it was\n" + response,
						diff.similar());
			} catch (IOException e) {
				e.printStackTrace();
				throw new AssertionFailedException("IOException during testing response.");
			} catch (SAXException e) {
				e.printStackTrace();
				throw new AssertionFailedException("SAXException during testing response.");
			} catch (SOAPException e) {
				e.printStackTrace();
				throw new AssertionFailedException("SOAPException during testing response.");
			}
		}

		private List<String> getRequestMessages(String dir) throws IOException {
			List<String> messages = new ArrayList<String>();
			File messages_dir = new File(ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID,
					MESSAGE_DIR + "/" + dir));
			String[] file = messages_dir.list();
			for (int i = 0; i < file.length; i++) {
				if (file[i].endsWith(".xml") && file[i].contains("request")) {
					messages.add(file[i]);
				}
			}
			return messages;
		}

		private String getMessageFromFile(String fileName) throws IOException {
			return getMessageFromFile(fileName, MESSAGE_DIR);
		}

		private String getMessageFromFile(String fileName, String dir) throws IOException {
			String path = dir + "/" + fileName;
			String message = readFile(ResourceHelper.getResourceAbsolutePath(Activator.PLUGIN_ID, path));
			assertNotNull("Couldn't get message from " + path, message);
			return message;
		}

		private String readFile(String file) throws IOException {
			if (file == null) {
				throw new NullPointerException("Couldn't read from null");
			}
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			StringBuilder stringBuilder = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
			}
			reader.close();
			return stringBuilder.toString();
		}

	}

}