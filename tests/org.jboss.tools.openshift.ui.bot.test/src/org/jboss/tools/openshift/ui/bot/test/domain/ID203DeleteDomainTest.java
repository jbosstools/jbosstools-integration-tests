package org.jboss.tools.openshift.ui.bot.test.domain;

import static org.junit.Assert.fail;

import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView;
import org.junit.After;
import org.junit.Test;

/**
 * Test capabilities of domain deletion via context menu.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID203DeleteDomainTest {

	private boolean domainDeleted = false;
	
	@Test
	public void testDeleteDomain() {
		domainDeleted = deleteDomain(Datastore.USERNAME, Datastore.DOMAIN);
		if (!domainDeleted) {
			fail("Domain has not been removed from OpenShift explorer view after deletion.");
		}
		// PASS
	}
	
	public static boolean deleteDomain(String username, String domain) {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.getDomain(username, domain).select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.DELETE_DOMAIN).select();
		
		new DefaultShell(OpenShiftLabel.Shell.DELETE_DOMAIN);
		
		new CheckBox(0).click();
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.DELETE_DOMAIN),
				TimePeriod.LONG);
		
		new WaitWhile(new JobIsRunning());
		
		try {
			explorer.getDomain(Datastore.USERNAME, Datastore.DOMAIN);
			return false;
		} catch (JFaceLayerException ex) {
			return true;
		}
	}
	
	@After
	public void recreateDomain() {
		if (domainDeleted) {
			createDomain(Datastore.USERNAME, Datastore.DOMAIN);
		}
	}
	
	public static void createDomain(String username, String domain) {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.getConnection(username).select();;
		new ContextMenu(OpenShiftLabel.ContextMenu.NEW_DOMAIN).select();

		new DefaultShell(OpenShiftLabel.Shell.CREATE_DOMAIN);
		new LabeledText(OpenShiftLabel.TextLabels.DOMAIN_NAME).setText(domain);
		
		new WaitUntil(new ButtonWithTextIsActive(new FinishButton()), TimePeriod.NORMAL);
		
		new FinishButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CREATE_DOMAIN), TimePeriod.LONG);
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		try {
			explorer.getDomain(username, domain);
		} catch (JFaceLayerException ex) {
			fail("Domain has not been recreated");
		}
	}
}
