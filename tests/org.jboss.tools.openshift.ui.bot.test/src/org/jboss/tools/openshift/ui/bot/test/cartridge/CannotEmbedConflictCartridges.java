package org.jboss.tools.openshift.ui.bot.test.cartridge;

import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.DeleteApplication;
import org.jboss.tools.openshift.ui.bot.test.application.wizard.NewApplicationTemplates;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftLabel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test capabilities of embed conflict cartridges. Respectively attempt to add jenkins 
 * client cartridge without having jenkins app. It would be nice to have also test
 * php-my-admin which requires MySQL DB, but there is not php-my-admin on OSE. 
 * 
 * @author mlabuda@redhat.com
 *
 */
public class CannotEmbedConflictCartridges {

	private String APP_NAME = "diyapp" + System.currentTimeMillis();
	
	@Before
	public void createApp() {
		new NewApplicationTemplates(false).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.DIY, APP_NAME, false, true, true);
	}
	
	@Test
	public void tryEmbedJenkinsCartridge() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		explorer.getApplication(APP_NAME).select();
		
		new ContextMenu(OpenShiftLabel.Labels.EDIT_CARTRIDGES).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EDIT_CARTRIDGES));
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_CARTRIDGES).setFocus();
		new DefaultTable().getItem(OpenShiftLabel.Cartridge.JENKINS).select();
		new DefaultTable().getItem(OpenShiftLabel.Cartridge.JENKINS).setChecked(true);
		
		new WaitUntil(new ShellWithTextIsAvailable("Add Cartridges"), TimePeriod.NORMAL);
		
		new DefaultShell("Add Cartridges").setFocus();
		new PushButton(OpenShiftLabel.Button.CANCEL).click();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EDIT_CARTRIDGES));
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_CARTRIDGES).setFocus();
		new PushButton(OpenShiftLabel.Button.FINISH).click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.NORMAL);
	}
	
	@After
	public void deleteApp() {
		new DeleteApplication(APP_NAME).perform();
	}
	
}
