package org.jboss.tools.openshift.ui.bot.test.explorer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.jboss.reddeer.junit.logging.Logger;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.view.impl.WorkbenchView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.util.TestProperties;
import org.junit.Before;
import org.junit.Test;

public class Connection {

	@Before
	public void setUpServer() {
		try {
			new WorkbenchView("Welcome").close();
		} catch (UnsupportedOperationException ex) {}
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		TestProperties.put("openshift.server.url", System.getProperty("libra.server"));
		TestProperties.put("openshift.user.name", System.getProperty("user.name"));
		TestProperties.put("openshift.user.pwd", System.getProperty("user.pwd"));
	}
	
	@Test
	public void canCreateConnectionToOpenShiftAccount() {
		OpenShiftExplorerView openshiftView = new OpenShiftExplorerView();
		openshiftView.openConnectionShell();
		
		// wrong credentials
		openshiftView.connectToOpenShift(TestProperties.get("openshift.server.url"), TestProperties.get("openshift.user.name"),
				TestProperties.get("openshift.user.wrongpwd"), false);
				
		// wait for credentials validation
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		assertFalse("Finish button shouldn't be enabled.",
				new PushButton("Finish").isEnabled());

		// set correct user credentials
		openshiftView.connectToOpenShift(TestProperties.get("openshift.server.url"), TestProperties.get("openshift.user.name"),
				TestProperties.get("openshift.user.pwd"), false);
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		openshiftView.open();
		assertTrue("Connection has not been established", new DefaultTree().getItems().size() > 0);
		
		Logger logger = new Logger(this.getClass());
		logger.info("*** OpenShift RedDeer Tests: Credentials validated. ***");
		logger.info("*** OpenShift RedDeer Tests: Connection to OpenShift established. ***");
	}
	
}
