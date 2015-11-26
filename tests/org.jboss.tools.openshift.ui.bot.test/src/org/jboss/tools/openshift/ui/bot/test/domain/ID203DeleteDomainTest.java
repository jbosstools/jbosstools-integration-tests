package org.jboss.tools.openshift.ui.bot.test.domain;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.jface.exception.JFaceLayerException;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShift2Connection;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
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
		domainDeleted = deleteDomain(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN);
		if (!domainDeleted) {
			fail("Domain has not been removed from OpenShift explorer view after deletion.");
		}
		// PASS
	}
	
	public static boolean deleteDomain(String username, String server, String domain) {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		OpenShift2Connection connection = explorer.getOpenShift2Connection(username, server);
		connection.getDomain(domain).select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.DELETE_DOMAIN).select();
		
		new DefaultShell(OpenShiftLabel.Shell.DELETE_DOMAIN);
		
		new CheckBox(0).click();
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.DELETE_DOMAIN),
				TimePeriod.LONG);
		
		new WaitWhile(new JobIsRunning());
		
		try {
			connection.getDomain(DatastoreOS2.DOMAIN);
			return false;
		} catch (JFaceLayerException ex) {
			return true;
		}
	}
	
	@After
	public void recreateDomain() {
		if (domainDeleted) {
			createDomain(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN);
		}
	}
	
	public static void createDomain(String username, String server, String domain) {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		OpenShift2Connection connection = explorer.getOpenShift2Connection(username, server);
		connection.select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.NEW_DOMAIN).select();

		new DefaultShell(OpenShiftLabel.Shell.CREATE_DOMAIN);
		new LabeledText(OpenShiftLabel.TextLabels.DOMAIN_NAME).setText(domain);
		
		new WaitUntil(new WidgetIsEnabled(new FinishButton()), TimePeriod.NORMAL);
		
		new FinishButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CREATE_DOMAIN), TimePeriod.LONG);
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		try {
			connection.getDomain(domain);
		} catch (JFaceLayerException ex) {
			fail("Domain has not been recreated");
		}
	}
}
