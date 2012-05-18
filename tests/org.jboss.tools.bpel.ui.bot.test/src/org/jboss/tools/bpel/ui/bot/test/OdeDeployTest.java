package org.jboss.tools.bpel.ui.bot.test;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.eclipse.core.resources.IFile;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.bpel.ui.bot.test.suite.BPELTest;
import org.jboss.tools.bpel.ui.bot.test.util.ResourceHelper;
import org.jboss.tools.bpel.util.SendSoapMessage;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.view.ServersView;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * 
 * @author psrna, apodhrad
 *
 */
@Require(server = @Server(type = ServerType.JbossAS, state = ServerState.Running, version = "5.1"), perspective="BPEL")
public class OdeDeployTest extends BPELTest {
	
	final static String BUNDLE   = "org.jboss.tools.bpel.ui.bot.test";
	final static String ENDPOINT = "http://localhost:8080/SayHelloProcess";
	final static String MESSAGE  = 
		"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
		"xmlns:q0=\"http://www.jboss.org/bpel/examples\" " +
		"xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"  " +
		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
		"	<soapenv:Header/>" +
		"	<soapenv:Body>" +
		"			<q0:SayHelloRequest>" +
		"				<q0:input>JBDS</q0:input>" +
		"			</q0:SayHelloRequest>" +
		"	</soapenv:Body>" +
		"</soapenv:Envelope>";

	final static String EXPECTED_RESPONSE = "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">"
			+ "  <SOAP-ENV:Header />"
			+ "  <SOAP-ENV:Body>"
			+ "    <SayHelloResponse xmlns=\"http://www.jboss.org/bpel/examples\">"
			+ "      <tns:result xmlns:tns=\"http://www.jboss.org/bpel/examples\">Hello JBDS</tns:result>"
			+ "    </SayHelloResponse>"
			+ "  </SOAP-ENV:Body>"
			+ "</SOAP-ENV:Envelope>";
	
	ServersView sView = new ServersView();

	
	@BeforeClass
	public static void setupWorkspace() throws Exception {
		ResourceHelper.importProject(BUNDLE, "/projects/bpel_say_hello", "say_hello");
		bot.viewByTitle("Project Explorer").setFocus();
	}
	
	@Test
	public void deploymentDescriptorTest() throws Exception {
		
		IFile deployFile = createNewDeployDescriptor("say_hello");

		bot.editorByTitle("deploy.xml").show();
		bot.editorByTitle("deploy.xml").setFocus();
		SWTBot editorBot = bot.editorByTitle("deploy.xml").bot();
		
		SWTBotTable table = editorBot.table(0);
		table.click(0, 1);
		bot.sleep(TIME_1S);
		
		editorBot.ccomboBox("-- none -- ").setSelection("SayHelloPort");
		table.click(0, 2);
		bot.editorByTitle("deploy.xml").save();
		
		String deployContent = loadFile(deployFile);
		Assert.assertTrue(deployContent != null);
		Assert.assertTrue(deployContent.contains("<deploy xmlns=\"http://www.apache.org/ode/schemas/dd/2007/03\" xmlns:examples=\"http://www.jboss.org/bpel/examples\">"));
		Assert.assertTrue(deployContent.contains("<process name=\"examples:HelloWorld\">"));
		Assert.assertTrue(deployContent.contains("<active>true</active>"));
		Assert.assertTrue(deployContent.contains("<retired>false</retired>"));
		Assert.assertTrue(deployContent.contains("<process-events generate=\"all\"/>"));
		Assert.assertTrue(deployContent.contains("<provide partnerLink=\"client\">"));
		Assert.assertTrue(deployContent.contains("<service name=\"examples:SayHelloService\" port=\"SayHelloPort\"/>"));
		Assert.assertTrue(deployContent.contains("</provide>"));
		Assert.assertTrue(deployContent.contains("</process>"));
		Assert.assertTrue(deployContent.contains("</deploy>"));
			
		bot.sleep(TIME_10S);
		
	}
	
	@Test
	public void deployProjectTest() throws Exception {
		
		String serverName = OdeDeployTest.configuredState.getServer().name;
		// Publish the process
		projExplorer.runOnServer("say_hello");

		Assert.assertFalse(console.getConsoleText().contains("DEPLOYMENTS IN ERROR:"));
		
		bot.viewByTitle("Servers").show();
		bot.viewByTitle("Servers").setFocus();
		
		SWTBotTree tree = bot.viewByTitle("Servers").bot().tree(); 
		SWTBotTreeItem server = tree.getTreeItem(serverName + "  [Started, Synchronized]").select();
		server.expand();
		bot.sleep(TIME_5S);
		assertTrue(isProjectDeployed("say_hello"));
	}
	
	@Test
	public void requestResponseTest() throws Exception {
		// Test the process
		String response = SendSoapMessage.sendMessage(ENDPOINT, MESSAGE, "simple");

		XMLUnit.setIgnoreWhitespace(true);
		Diff diff = new Diff(response, EXPECTED_RESPONSE);

		assertTrue(diff.similar());
	}
	
}
