package org.jboss.tools.deltacloud.ui.bot.test;


import java.util.Date;

import org.jboss.tools.deltacloud.ui.bot.test.view.CloudViewer;
import org.jboss.tools.deltacloud.ui.bot.test.view.ConnectionWizard;
import org.jboss.tools.ui.bot.ext.Assertions;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
@Require(runOnce=true,perspective="Deltacloud",secureStorage=true)
public class ConnectionWizardTest extends SWTTestExt {

	private CloudViewer cloudViewer = new CloudViewer();
	private static final String connectionName=Util.CONNECTION_DEFAULT_NAME+"2";
	
	@BeforeClass
	public static void setUpInstances() throws Exception {
		Util.setupDefaultConnection(new CloudViewer());
		new CloudViewer().addConnection(connectionName, Util.MOCK_URL, Util.MOCK_USER, Util.MOCK_PASS);
	}
	
	@AfterClass
	public static void waitAMinute() {
		//bot.sleep(Long.MAX_VALUE);
	}
	
	@Test
	public void invalidURL() {
		ConnectionWizard shell = cloudViewer.getConnection(connectionName).edit();
		shell.setURL("xxx");
		bot.sleep(Timing.time3S());
		assertTrue("Wizard does not show warning when invalid cloud URL is entered", shell.getErrorWarningText().startsWith("Invalid"));
		Assertions.assertControlEnabled(shell.bot().button(IDELabel.Button.TEST), false);
		shell.setURL("http://localhost:60000");
		bot.sleep(Timing.time10S());
		assertTrue("Wizard does not show warning when invalid cloud URL is entered", shell.getErrorWarningText().startsWith("Invalid"));
		Assertions.assertControlEnabled(shell.bot().button(IDELabel.Button.TEST), false);
		shell.cancel();
	}
	
	@Test
	public void testConnection() {
		ConnectionWizard shell = cloudViewer.getConnection(connectionName).edit();
		shell.setPassword("");
		shell.setUsername("");
		Assertions.assertControlEnabled(shell.bot().button(IDELabel.Button.TEST), true);
		shell.testConnection();
		// test should fail - credentials not provided
		assertTrue("Connection Test did not fail for empty credentials", shell.getErrorWarningText().contains("failed"));
		shell.setUsername(Util.MOCK_USER);
		shell.setPassword(Util.MOCK_PASS);
		shell.testConnection();
		assertFalse("Connection Test failed for valid credentials and URI", "".equals(shell.getErrorWarningText()));
		shell.cancel();
		
	}
	@Test 
	public void newConnection() {
		cloudViewer.show().bot().tree().contextMenu("New Connection").click();
		ConnectionWizard shell = new ConnectionWizard();		
		shell.setURL("");
		Assertions.assertControlEnabled(shell.bot().button(IDELabel.Button.TEST), false);
		Assertions.assertControlEnabled(shell.bot().button(IDELabel.Button.FINISH), false);
		shell.setURL(Util.MOCK_URL);
		shell.bot().sleep(TIME_1S);
		Assertions.assertControlEnabled(shell.bot().button(IDELabel.Button.TEST), true);
		Assertions.assertControlEnabled(shell.bot().button(IDELabel.Button.FINISH), false);
		shell.setName(Util.CONNECTION_DEFAULT_NAME);
		Assertions.assertControlEnabled(shell.bot().button(IDELabel.Button.FINISH), false);
		shell.setName(Util.CONNECTION_DEFAULT_NAME+new Date().getTime());
		Assertions.assertControlEnabled(shell.bot().button(IDELabel.Button.FINISH), true);
		shell.setURL(Util.MOCK_URL+"x");
		shell.bot().sleep(TIME_1S);
		Assertions.assertControlEnabled(shell.bot().button(IDELabel.Button.TEST), false);
		Assertions.assertControlEnabled(shell.bot().button(IDELabel.Button.FINISH), true);
		shell.setURL(Util.MOCK_URL);
		shell.bot().sleep(TIME_1S);
		Assertions.assertControlEnabled(shell.bot().button(IDELabel.Button.TEST), true);
		Assertions.assertControlEnabled(shell.bot().button(IDELabel.Button.FINISH), true);
		shell.finish();
	}
	
	
}
