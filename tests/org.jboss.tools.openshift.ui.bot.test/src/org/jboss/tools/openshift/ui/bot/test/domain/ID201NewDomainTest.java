package org.jboss.tools.openshift.ui.bot.test.domain;

import static org.junit.Assert.fail;

import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView;
import org.junit.Test;

/** 
 * Test creating a new domain via context menu of connection.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID201NewDomainTest {

	@Test
	public void testNewDomain() {
		createDomain(Datastore.USERNAME, Datastore.DOMAIN);
	}
	
	public static void createDomain(String username, String domainName) {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.getConnection(username).select();
		
		// Sometimes there is shown progress information shell
		try {
			new WaitUntil(new ShellWithTextIsAvailable("Progress Information"), TimePeriod.NORMAL);
			new WaitWhile(new ShellWithTextIsAvailable("Progress Information"), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
		}
		explorer.getConnection(username).select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.NEW_DOMAIN).select();

		new DefaultShell(OpenShiftLabel.Shell.CREATE_DOMAIN);
		new LabeledText(OpenShiftLabel.TextLabels.DOMAIN_NAME).setText(domainName);
		
		new WaitUntil(new ButtonWithTextIsActive(new FinishButton()), TimePeriod.NORMAL);
		
		new FinishButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CREATE_DOMAIN), TimePeriod.LONG);
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		explorer.getConnection(username).select();
		new ContextMenu(OpenShiftLabel.ContextMenu.REFRESH).select();

		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		try {
			explorer.getDomain(username, domainName);
		} catch (JFaceLayerException ex) {
			fail("Domain has not been created");
		}
	}
}
