package org.jboss.tools.openshift.ui.bot.test.connection;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.NoButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView;
import org.junit.Test;

/**
 * Test capabilities of secured storage for OpenShift Tools plugin.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID110SecurityStorageTest {

	private String pass = "1234";
	private OpenShiftExplorerView explorer;
	
	@Test
	public void testPasswordsSecuredStorage() {
		storePassword();
		
		verifySecurityStorage(true);
		
		removePassword();
		
		verifySecurityStorage(false);
	}
	
	private void storePassword() {
		explorer = new OpenShiftExplorerView();
		TreeItem connection = explorer.getConnection(Datastore.USERNAME);
		connection.select();
		new ContextMenu(OpenShiftLabel.ContextMenu.EDIT_CONNECTION).select();
		
		new DefaultShell("").setFocus();
		
		new CheckBox(0).click();
		new FinishButton().click();
		
		new DefaultShell(OpenShiftLabel.Shell.SECURE_STORAGE_PASSWORD).setFocus();
		
		new DefaultText(0).setText(pass);
		boolean firstStorage = true;
		try {
			new DefaultText(1).setText(pass);
		} catch (SWTLayerException ex) {
			firstStorage = false;
		}
		
		new WaitUntil(new ButtonWithTextIsActive(new OkButton()),TimePeriod.NORMAL);
		
		new OkButton().click();
		
		if (firstStorage) {
			new DefaultShell(OpenShiftLabel.Shell.SECURE_STORAGE).setFocus(); 
			new NoButton().click();
		}
		
		new WaitWhile(new JobIsRunning(), TimePeriod.getCustom(20));
	}
	
	private void verifySecurityStorage(boolean shouldExists) {
		new WorkbenchShell().setFocus();
		new ShellMenu("Window", "Preferences").select();
		
		new DefaultShell("Preferences");
		new WorkbenchPreferenceDialog().select("General", "Security", "Secure Storage");		
		new DefaultTabItem("Contents").activate();
		
		new DefaultTreeItem(new DefaultTree(1), "[Default Secure Storage]", "org.jboss.tools.openshift.express.ui",
				Datastore.SERVER, Datastore.USERNAME).select();
		
		// Wheee - reactivation required
		new DefaultTabItem("Contents").activate();
		
		boolean exists = new DefaultTable(0).getItems().size() == 1;

		new OkButton().click();
		
		assertTrue("Password storage verification failed", 
				shouldExists == exists);
	}
	
	private void removePassword() {
		TreeItem connection = explorer.getConnection(Datastore.USERNAME);
		connection.select();
		new ContextMenu(OpenShiftLabel.ContextMenu.EDIT_CONNECTION).select();
		
		new DefaultShell("").setFocus();
		
		new CheckBox(0).click();
		new FinishButton().click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.getCustom(20));
	}
	
	
}
