package org.jboss.tools.openshift.ui.bot.test.connection;

import static org.junit.Assert.assertFalse;

import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftExplorerView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Connection to OpenShift enterprise has different steps - while establishing connection
 * there is a certificate acceptance dialog that user accept certificate of the given OSE.
 * @author mlabuda
 *
 */
public class ConnectionEnterprise {
	
	@Before
	public void setUpServer() {
		Connection.prepareTest();
	}
	
	@Test
	public void canCreateConnectionToOpenShiftAccount() {
		OpenShiftExplorerView openshiftView = new OpenShiftExplorerView();
		openshiftView.openConnectionShell();
		
		// wrong credentials
		openshiftView.connectToOpenShift(System.getProperty("libra.server"), 
				System.getProperty("user.name"), "wtffailpwd", false);
				
		acceptCertificate();
		
		// wait for credentials validation
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		assertFalse("Finish button shouldn't be enabled.",
				new PushButton("Finish").isEnabled());

		// set correct user credentials
		openshiftView.connectToOpenShift(System.getProperty("libra.server"), 
				System.getProperty("user.name"), System.getProperty("user.pwd"), false);
	}
	
	private void acceptCertificate() {
			new WaitUntil(new ShellWithTextIsAvailable("Untrusted SSL Certificate"),
				TimePeriod.NORMAL);
			
			new DefaultShell("Untrusted SSL Certificate").setFocus();
			
			new PushButton("Yes").click();
	}
	
	@After
	public void verifyConnection() {
		Connection.verifyConnectionEstablishment();
	}
	
}
