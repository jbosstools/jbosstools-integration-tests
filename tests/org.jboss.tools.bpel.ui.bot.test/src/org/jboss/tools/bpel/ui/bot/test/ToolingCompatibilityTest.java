package org.jboss.tools.bpel.ui.bot.test;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;
import static org.eclipse.swtbot.swt.finder.waits.Conditions.waitForWidget;

import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swtbot.eclipse.gef.finder.SWTGefBot;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditPart;
import org.eclipse.swtbot.eclipse.gef.finder.widgets.SWTBotGefEditor;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.utils.MessageFormat;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.eclipse.swtbot.swt.finder.widgets.AbstractSWTBotControl;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.ui.internal.views.properties.tabbed.view.TabbedPropertyList.ListElement;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.jboss.tools.bpel.ui.bot.ext.widgets.BotBpelEditor;
import org.jboss.tools.bpel.ui.bot.test.suite.BPELTest;
import org.jboss.tools.bpel.ui.bot.test.util.CompositeControl;
import org.jboss.tools.bpel.ui.bot.test.util.ResourceHelper;
import org.jboss.tools.bpel.util.SendSoapMessage;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerType;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.view.PropertiesView;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


@Require(server = @Server(type = ServerType.SOA, state = ServerState.Running), perspective="BPEL")
public class ToolingCompatibilityTest extends BPELTest{
	
	final static String BUNDLE   = "org.jboss.tools.bpel.ui.bot.test";
	final static String ENDPOINT = "http://localhost:8080/ode/processes/HelloWorld";
	final static String MESSAGE  = 
		"<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
		"xmlns:q0=\"http://helloWorld\" " +
		"xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"  " +
		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
		"	<soapenv:Header/>" +
		"	<soapenv:Body>" +
		"			<q0:HelloWorldRequest>" +
		"				<q0:input>Kitty</q0:input>" +
		"			</q0:HelloWorldRequest>" +
		"	</soapenv:Body>" +
		"</soapenv:Envelope>";
	
	
	@BeforeClass
	public static void setupWorkspace() throws Exception {
		ResourceHelper.importProject(BUNDLE, "/projects/eclipse_tooling_proj", "eclipse_tooling_proj");
		bot.viewByTitle("Project Explorer").setFocus();
	}
	
	
	@Test
	public void deployEclipseProjectTest(){
		
		String serverName = ToolingCompatibilityTest.configuredState.getServer().name;
		// Publish the process
		projExplorer.runOnServer("eclipse_tooling_proj");

		Assert.assertFalse(console.getConsoleText().contains("DEPLOYMENTS IN ERROR:"));
		Assert.assertFalse(console.getConsoleText().contains("deploy failed"));
		
		bot.viewByTitle("Servers").show();
		bot.viewByTitle("Servers").setFocus();
		
		SWTBotTree tree = bot.viewByTitle("Servers").bot().tree(); 
		SWTBotTreeItem server = tree.getTreeItem(serverName + "  [Started, Synchronized]").select();
		server.expand();
		bot.sleep(TIME_5S);
		assertTrue(server.getNode("eclipse_tooling_proj  [Started, Synchronized]").isVisible());
	}
	
	@Test
	public void requestResponseTest() throws Exception {
		
		// Test the process
		String response = SendSoapMessage.sendMessage(ENDPOINT, MESSAGE, "simple");
		
		Assert.assertTrue(response != null);
		Assert.assertTrue(response.contains("<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">"));
		Assert.assertTrue(response.contains("<SOAP-ENV:Header />"));
		Assert.assertTrue(response.contains("<SOAP-ENV:Body>"));
		Assert.assertTrue(response.contains("<HelloWorldResponse xmlns=\"http://helloWorld\">"));
		Assert.assertTrue(response.contains("<tns:result xmlns:tns=\"http://helloWorld\">Kitty</tns:result>"));
		Assert.assertTrue(response.contains("</HelloWorldResponse>"));
		Assert.assertTrue(response.contains("</SOAP-ENV:Body>"));
		Assert.assertTrue(response.contains("</SOAP-ENV:Envelope>"));

	}
	

	@SuppressWarnings({ "static-access", "restriction" })
	@Test
	public void simpleEditingTest() throws Exception {
		
		openFile("eclipse_tooling_proj", "bpelContent", "HelloWorld.bpel");
		
		SWTGefBot gefBot = new SWTGefBot();
		final SWTBotGefEditor editor = gefBot.gefEditor("HelloWorld.bpel");
		final BotBpelEditor bpel = new BotBpelEditor(editor, gefBot);
		bpel.activatePage("Design");
		
		log.info("Opening Properties View");
		PropertiesView pView = new PropertiesView();
		pView.show();
		pView.show().setFocus();
		log.info("Properties View opened!");
		
		log.info("Click on \"Assign\"");
		bpel.getEditPart(bpel.getSelectedPart(), "Assign").click();
		
		Matcher matcher = widgetOfType(ListElement.class);
		List<ListElement> tabs = pView.bot().widgets(matcher);		
		Composite foundtab = tabs.get(1);
		CompositeControl mw = new CompositeControl(foundtab, matcher);
		mw.click();
		
		log.info("Selecting Variable to Variable from the list");
		pView.bot().list().select("Variable to Variable");
		log.info("ComboBox: choose 'Expression'");
		pView.bot().comboBoxWithLabel("From:").setSelection("Expression");
		pView.bot().styledText().setText("concat('Hello ', $input.payload/tns:input)");
		log.info("Save");
		bpel.save();
		bot.sleep(TIME_5S);
		
		log.info("Remove from server");
		removeFromServer("eclipse_tooling_proj");
		bot.sleep(TIME_5S);
		log.info("Run on server");
		projExplorer.runOnServer("eclipse_tooling_proj");
		
		String serverName = OdeDeployTest.configuredState.getServer().name;

		Assert.assertFalse(console.getConsoleText().contains("DEPLOYMENTS IN ERROR:"));
		Assert.assertFalse(console.getConsoleText().contains("deploy failed"));
		
		bot.viewByTitle("Servers").show();
		bot.viewByTitle("Servers").setFocus();
		
		SWTBotTree tree = bot.viewByTitle("Servers").bot().tree(); 
		SWTBotTreeItem server = tree.getTreeItem(serverName + "  [Started, Synchronized]").select();
		server.expand();
		bot.sleep(TIME_5S);
		assertTrue(server.getNode("eclipse_tooling_proj  [Started, Synchronized]").isVisible());
		
	}
	
	@Test
	public void requestResponseEditedTest() throws Exception {
		
		// Test the process
		String response = SendSoapMessage.sendMessage(ENDPOINT, MESSAGE, "simple");
		
		Assert.assertTrue(response != null);
		Assert.assertTrue(response.contains("<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">"));
		Assert.assertTrue(response.contains("<SOAP-ENV:Header />"));
		Assert.assertTrue(response.contains("<SOAP-ENV:Body>"));
		Assert.assertTrue(response.contains("<HelloWorldResponse xmlns=\"http://helloWorld\">"));
		Assert.assertTrue(response.contains("<tns:result xmlns:tns=\"http://helloWorld\">Hello Kitty</tns:result>"));
		Assert.assertTrue(response.contains("</HelloWorldResponse>"));
		Assert.assertTrue(response.contains("</SOAP-ENV:Body>"));
		Assert.assertTrue(response.contains("</SOAP-ENV:Envelope>"));

	}
	
	
	public void removeFromServer(String projectName){
		String serverName = ToolingCompatibilityTest.configuredState.getServer().name;
		
		bot.viewByTitle("Servers").show();
		bot.viewByTitle("Servers").setFocus();
		
		SWTBotTree tree = bot.viewByTitle("Servers").bot().tree(); 
		bot.sleep(TIME_5S);
		SWTBotTreeItem server = tree.getTreeItem(serverName + "  [Started, Synchronized]").select();
		
		ContextMenuHelper.prepareTreeItemForContextMenu(tree, server);
		new SWTBotMenu(ContextMenuHelper.getContextMenu(tree, IDELabel.Menu.ADD_AND_REMOVE, false)).click();
		
		SWTBotShell shell = OdeDeployTest.bot.shell("Add and Remove...");
		shell.activate();
		
		
		SWTBot viewBot = shell.bot();
		viewBot.tree(1).setFocus();
		viewBot.tree(1).select(projectName);
		viewBot.button("< Remove").click();
		viewBot.button("Finish").click();

	}

}
