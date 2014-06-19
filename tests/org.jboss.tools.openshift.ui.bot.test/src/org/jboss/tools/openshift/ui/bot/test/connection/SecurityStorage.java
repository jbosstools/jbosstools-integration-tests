package org.jboss.tools.openshift.ui.bot.test.connection;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.Test;

public class SecurityStorage {

	private String pass = "1234";
	
	@Test
	public void storeVerifyRemovePassword() {
		storePassword();
		
		verifySecurityStorage(true);
		
		removePassword();
		
		verifySecurityStorage(false);
	}
	
	private void storePassword() {
		TreeItem connection = new OpenShiftExplorerView().getConnection();
		connection.select();
		new ContextMenu("Edit Connection...").select();
		
		new DefaultShell("").setFocus();
		
		new CheckBox(0).click();
		new PushButton(OpenShiftLabel.Button.FINISH).click();
		
		new DefaultShell("Secure Storage Password").setFocus();
		
		new DefaultText(0).setText(pass);
		boolean firstStorage = true;
		try {
			new DefaultText(1).setText(pass);
		} catch (SWTLayerException ex) {
			firstStorage = false;
		}
		
		new WaitUntil(new ButtonWithTextIsActive(new PushButton(OpenShiftLabel.Button.OK)),
				TimePeriod.NORMAL);
		
		new PushButton(OpenShiftLabel.Button.OK).click();
		
		if (firstStorage) {
			new DefaultShell("Secure Storage").setFocus(); 
			new PushButton(OpenShiftLabel.Button.NO).click();
		}
		
		new WaitWhile(new JobIsRunning(), TimePeriod.getCustom(20));
	}
	
	private void verifySecurityStorage(boolean shouldExists) {
		new WorkbenchShell().setFocus();
		new ShellMenu("Window", "Preferences").select();
		
		new DefaultShell("Preferences");
		
		new DefaultTreeItem("General", "Security", "Secure Storage").select();		
		new DefaultTabItem("Contents").activate();
		
		new DefaultTreeItem(1, "[Default Secure Storage]", "org.jboss.tools.openshift.express.ui",
				System.getProperty("libra.server"), System.getProperty("user.name")).select();
		
		// Wheee - reactivation required
		new DefaultTabItem("Contents").activate();
		
		boolean exists = new DefaultTable(0).getItems().size() == 1;

		new PushButton(OpenShiftLabel.Button.OK).click();
		
		assertTrue("Password storage verification failed", 
				shouldExists == exists);
	}
	
	private void removePassword() {
		TreeItem connection = new OpenShiftExplorerView().getConnection();
		connection.select();
		new ContextMenu("Edit Connection...").select();
		
		new DefaultShell("").setFocus();
		
		new CheckBox(0).click();
		new PushButton(OpenShiftLabel.Button.FINISH).click();
	}
	
	
}
