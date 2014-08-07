package org.jboss.tools.openshift.ui.bot.test.application.handle;

import static org.junit.Assert.*;

import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.openshift.ui.bot.test.application.create.IDXXXCreateTestingApplication;
import org.jboss.tools.openshift.ui.condition.ApplicationIsDeployedSuccessfully;
import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView;
import org.junit.Test;

/**
 * Test capabilities of showing an application in web browser.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID708ShowInBrowserTest extends IDXXXCreateTestingApplication {

	@Test
	public void testShowInBrowser() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.getApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName).select();
		
		showInBrowser(applicationName);
	}
	
	/**
	 * Assert, that tree item is selected before invoking this method.
	 * 
	 * @param applicationName application name
	 */
	public static void showInBrowser(String applicationName) {
		AbstractWait.sleep(TimePeriod.getCustom(5));
		new ContextMenu(OpenShiftLabel.ContextMenu.SHOW_IN_BROWSER).select();
		
		AbstractWait.sleep(TimePeriod.getCustom(8));
		
		try {
			new WaitUntil(new ApplicationIsDeployedSuccessfully(Datastore.USERNAME, Datastore.DOMAIN,
					applicationName, "OpenShift"), TimePeriod.LONG);
		} catch (SWTLayerException ex) {
			fail("Browser was not opened successfully.");
		} catch (WaitTimeoutExpiredException ex) {
			fail("Application has not been shown successfully in browser.");
		}
	}
}
