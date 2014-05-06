package org.jboss.tools.openshift.ui.bot.test.cartridge;

import java.util.Date;

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
 * Test embedding CRON cartridge into an application.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class EmbedCartridge {

	private final String DIY_APP = "diyapp" + new Date().getTime();

	@Before
	public void createDYIApp() {
		new NewApplicationTemplates(false).createSimpleApplicationWithoutCartridges(
				OpenShiftLabel.AppType.DIY, DIY_APP, false, true, true);
	}

	@Test
	public void canEmbedCartridge() {
		embedCartrige(DIY_APP, OpenShiftLabel.Cartridge.CRON);
	}

	
	public static void embedCartrige(String appName, String cartridge) {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();

		explorer.getApplication(appName).select();
		
		new ContextMenu(OpenShiftLabel.Labels.EDIT_CARTRIDGES).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EDIT_CARTRIDGES));
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_CARTRIDGES).setFocus();
		new DefaultTable().getItem(cartridge).setChecked(true);
		new PushButton(OpenShiftLabel.Button.FINISH).click();
		
		new WaitUntil(new ShellWithTextIsAvailable("Embedded Cartridges"), TimePeriod.VERY_LONG);
		
		new DefaultShell("Embedded Cartridges").setFocus();
		new PushButton(OpenShiftLabel.Button.OK).click();
	
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	@After
	public void deleteDIYApp() {
		new DeleteApplication(DIY_APP).perform();
	}

}
