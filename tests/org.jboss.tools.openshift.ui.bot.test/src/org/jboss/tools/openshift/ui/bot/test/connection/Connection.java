package org.jboss.tools.openshift.ui.bot.test.connection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.junit.logging.Logger;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftExplorerView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test create a new connection to OpenShift Online server
 * 
 * @author mlabuda@redhat.com
 *
 */
public class Connection {

	@Before
	public void setUpServer() {
		prepareTest();
	}
	
	public static void prepareTest() {
		try {
			new WorkbenchView("Welcome").close();
		} catch (UnsupportedOperationException ex) {}
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	@Test
	public void canCreateConnectionToOpenShiftAccount() {
		createConnectionToOpenShift();
	}
	
	public static void createConnectionToOpenShift() {
		OpenShiftExplorerView openshiftView = new OpenShiftExplorerView();
		openshiftView.open();
		openshiftView.openConnectionShell();
		
		// wrong credentials
		openshiftView.connectToOpenShift(System.getProperty("libra.server"), 
				System.getProperty("user.name"), "wtffailpwd", false);
				
		// wait for credentials validation
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		assertFalse("Finish button shouldn't be enabled.",
				new PushButton("Finish").isEnabled());

		// set correct user credentials
		openshiftView.connectToOpenShift(System.getProperty("libra.server"), 
				System.getProperty("user.name"), System.getProperty("user.pwd"), false);
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	@After
	public void verifyConnection() {
		AbstractWait.sleep(TimePeriod.SHORT);
		verifyConnectionEstablishment();
	}
	
	public static void verifyConnectionEstablishment() {
		new OpenShiftExplorerView().open();
		// Sometimes it takes times
		AbstractWait.sleep(TimePeriod.SHORT);
		
		assertTrue("Connection has not been established", new DefaultTree().getItems().size() > 0);
		
		Logger logger = new Logger(Connection.class);
		logger.info("*** OpenShift RedDeer Tests: Credentials validated. ***");
		logger.info("*** OpenShift RedDeer Tests: Connection to OpenShift established. ***");
	}
}
