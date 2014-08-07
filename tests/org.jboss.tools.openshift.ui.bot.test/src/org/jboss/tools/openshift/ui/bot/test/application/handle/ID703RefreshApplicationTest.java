package org.jboss.tools.openshift.ui.bot.test.application.handle;

import static org.junit.Assert.*;

import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.bot.test.application.create.IDXXXCreateTestingApplication;
import org.jboss.tools.openshift.ui.condition.ApplicationIsDeployedSuccessfully;
import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView;
import org.junit.Test;

/**
 * Test capabilities of refreshing an application.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID703RefreshApplicationTest extends IDXXXCreateTestingApplication {

	@Test
	public void testRefreshApplication() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.getApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName).select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.REFRESH).select();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		
		try {
			new WaitUntil(new ApplicationIsDeployedSuccessfully(Datastore.USERNAME, Datastore.DOMAIN, 
				applicationName, "OpenShift"), TimePeriod.VERY_LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Application has not been refreshed successfully.");
		}
	}
}
