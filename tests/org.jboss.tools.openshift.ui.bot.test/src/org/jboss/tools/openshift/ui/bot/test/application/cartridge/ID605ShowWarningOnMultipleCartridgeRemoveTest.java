package org.jboss.tools.openshift.ui.bot.test.application.cartridge;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsEnabled;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.test.application.create.IDXXXCreateTestingApplication;
import org.jboss.tools.openshift.ui.bot.test.util.Datastore;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.junit.Test;

public class ID605ShowWarningOnMultipleCartridgeRemoveTest extends IDXXXCreateTestingApplication {

	OpenShiftExplorerView explorer = new OpenShiftExplorerView();

	@Test
	public void deselectEmbeddedCartridge() {
		embedCartridge(OpenShiftLabel.EmbeddableCartridge.CRON);		
		
		explorer.selectApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName);
		new ContextMenu(OpenShiftLabel.ContextMenu.EMBED_CARTRIDGE).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EDIT_CARTRIDGES));
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_CARTRIDGES);
		
		new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.CRON).select();
		new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.CRON).setChecked(false);
		
		try {
			new WaitUntil(new ShellWithTextIsAvailable("Remove cartridge cron-1.4"));
			
			new DefaultShell("Remove cartridge cron-1.4");
			new YesButton().click();
			
			new DefaultShell(OpenShiftLabel.Shell.EDIT_CARTRIDGES);
			assertFalse("Cartridge should be unchecked after its removing, but it is still presented.",
					new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.CRON).isChecked()); 
			
			
		} catch (WaitTimeoutExpiredException ex) {
			fail("There should be warning dialog about possible data loss, but dialog is missing.");
		}
		
		new CancelButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EDIT_CARTRIDGES));
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	
	@Test
	public void deselectMultipleEmbeddedCartridges() {
		embedCartridge(OpenShiftLabel.EmbeddableCartridge.CRON);
		embedCartridge(OpenShiftLabel.EmbeddableCartridge.POSTGRE_SQL);
		
		explorer.selectApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName);
		new ContextMenu(OpenShiftLabel.ContextMenu.EMBED_CARTRIDGE).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EDIT_CARTRIDGES));
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_CARTRIDGES);
		
		new PushButton("Deselect All").click();
		
		try {
			new WaitUntil(new ShellWithTextIsAvailable("Deselect All Cartridges"));
			
			new DefaultShell("Deselect All Cartridges");
			new YesButton().click();
			
			new DefaultShell(OpenShiftLabel.Shell.EDIT_CARTRIDGES);
			
			assertFalse("Cartridge has been removed, it should not be listed as checked anymore",
					new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.CRON).isChecked());
			assertFalse("Cartridge has been removed, it should not be listed as checked anymore",
					new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.POSTGRE_SQL).isChecked());
		} catch (WaitTimeoutExpiredException ex) {
			fail("There should be warning dialog about possible data loss, but dialog is missing.");
		}
		
		new CancelButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EDIT_CARTRIDGES));
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	@Test
	public void deselectMultipleCurrentlyAddedCartridges() {
		explorer.open();
		explorer.selectApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName);
		new ContextMenu(OpenShiftLabel.ContextMenu.EMBED_CARTRIDGE).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EDIT_CARTRIDGES));
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_CARTRIDGES);
		
		new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.CRON).select();
		new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.CRON).setChecked(true);
		
		new PushButton("Deselect All").click();
		
		try {
			new WaitUntil(new ShellWithTextIsAvailable("Deselect All Cartridges"), TimePeriod.getCustom(5));
			fail("There should not be shown warning about possible data loss on freshly added cartridges.");
		} catch (WaitTimeoutExpiredException ex) {
			// PASS
		}
		
		new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.CRON).select();
		assertFalse("Cartridge should be listed as unchecked, because it was removed.",
				new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.CRON).isChecked());
		
		new CancelButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EDIT_CARTRIDGES));
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	private void embedCartridge(String cartridge) {
		explorer.open();
		explorer.selectApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName);
		new ContextMenu(OpenShiftLabel.ContextMenu.EMBED_CARTRIDGE).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EDIT_CARTRIDGES));
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_CARTRIDGES);
		
		new DefaultTable().getItem(cartridge).select();
		new DefaultTable().getItem(cartridge).setChecked(true);
		
		new WaitUntil(new ButtonWithTextIsEnabled(new FinishButton()), TimePeriod.LONG);
		
		new FinishButton().click();
		
		try {
			new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EMBEDDED_CARTRIDGE),
					TimePeriod.LONG);
		
			new DefaultShell(OpenShiftLabel.Shell.EMBEDDED_CARTRIDGE);
			new OkButton().click();
		} catch (WaitTimeoutExpiredException ex) {
			// PASS - there is no embed cartridge shell showing info about cartridge
		}
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EDIT_CARTRIDGES));
	}
}
