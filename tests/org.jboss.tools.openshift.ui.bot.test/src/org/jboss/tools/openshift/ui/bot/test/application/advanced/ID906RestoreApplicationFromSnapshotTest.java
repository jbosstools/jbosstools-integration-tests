package org.jboss.tools.openshift.ui.bot.test.application.advanced;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.openshift.reddeer.condition.v2.ApplicationIsDeployedSuccessfully;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS2;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.test.application.create.IDXXXCreateTestingApplication;
import org.jboss.tools.openshift.ui.bot.test.application.handle.ID701ModifyAndRepublishApplicationTest;
import org.junit.Test;

/**
 * Test capabilities of restoring an application from stored snapshot. 
 * Verification is included.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID906RestoreApplicationFromSnapshotTest extends IDXXXCreateTestingApplication {
	
	@Test
	public void testRestoreApplicationFromSnapshot() {
		createSnapshot();
		
		ID701ModifyAndRepublishApplicationTest.modifyAndRepublishApplication(applicationName);
		
		restoreApplication();
	}
	
	private void createSnapshot() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).getDomain(DatastoreOS2.DOMAIN).
			getApplication(applicationName).select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.SAVE_SNAPSHOT).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.SAVE_SNAPSHOT),
				TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.SAVE_SNAPSHOT);
		new PushButton(OpenShiftLabel.Button.WORKSPACE).click();
		
		new DefaultShell(OpenShiftLabel.Shell.SELECT_EXISTING_PROJECT);
		new DefaultTable().getItem(applicationName).select();
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.SELECT_EXISTING_PROJECT),
				TimePeriod.NORMAL);
		
		new FinishButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.SAVE_SNAPSHOT),
				TimePeriod.VERY_LONG);
	}
	
	private void restoreApplication() { 
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).getDomain(DatastoreOS2.DOMAIN).
			getApplication(applicationName).select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.RESTORE_SNAPSHOT).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.RESTORE_SNAPSHOT),
				TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.RESTORE_SNAPSHOT);
		
		assertTrue("Stored snapshot has not been preselected for restoration.",
			new LabeledText("File:").getText().contains(applicationName + "-full.tar.gz"));
		
		new FinishButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.RESTORE_SNAPSHOT),
				TimePeriod.LONG);
		
		// It takes some time to restore
		AbstractWait.sleep(TimePeriod.getCustom(5));
		
		try {
			new WaitUntil(new ApplicationIsDeployedSuccessfully(DatastoreOS2.USERNAME, DatastoreOS2.SERVER,
				DatastoreOS2.DOMAIN, applicationName, "OpenShift"), TimePeriod.VERY_LONG);
			// PASS
		} catch (WaitTimeoutExpiredException ex) {
			fail("Application has not been restored successfully. Browser does not "
					+ "contain text of existing project which has been restored.");
		}
	}
}
