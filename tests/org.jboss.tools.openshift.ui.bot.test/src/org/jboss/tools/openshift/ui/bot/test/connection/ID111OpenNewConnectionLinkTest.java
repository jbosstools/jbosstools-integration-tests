package org.jboss.tools.openshift.ui.bot.test.connection;

import static org.junit.Assert.fail;

import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.impl.link.DefaultLink;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.openshift.ui.condition.BrowserContainsText;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView;
import org.junit.After;
import org.junit.Test;

/**
 * Test opening a new account link in connection shell.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID111OpenNewConnectionLinkTest {

	@Test
	public void testNewUserLink() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		explorer.openConnectionShell();
		
		new DefaultShell("");
		
		new DefaultLink().click();
		
		try {
			new WaitUntil(new BrowserContainsText("Create an account"), TimePeriod.LONG);
			// PASS
		} catch (WaitTimeoutExpiredException ex) {
			fail("Registration page has not been opened successfully.");
		}
	}
	
	@After
	public void closeBrowser() {
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				new InternalBrowser().getSWTWidget().close();
			}
		});
	}
}
