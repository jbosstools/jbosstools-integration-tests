package org.jboss.tools.openshift.ui.bot.test.application.cartridge;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.NoButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.jboss.tools.openshift.reddeer.view.OpenShift2Application;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.wizard.v2.ApplicationCreator;
import org.jboss.tools.openshift.ui.bot.test.util.DatastoreOS2;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ID602oAddConflictCartridgesTest {

	private String applicationName = "eap" + System.currentTimeMillis();	
	
	@Before
	public void createApplication() {
		new ApplicationCreator(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, false).
			createSimpleApplicationOnBasicCartridges(
				OpenShiftLabel.Cartridge.JBOSS_EAP, applicationName, true, true, true);
	}
	
	@Test
	public void testAddConflictCartridge() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		OpenShift2Application application = explorer.getOpenShift2Connection(DatastoreOS2.USERNAME, DatastoreOS2.SERVER).
				getDomain(DatastoreOS2.DOMAIN).getApplication(applicationName);
		application.select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.EMBED_CARTRIDGE).select();
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_CARTRIDGES);
		
		// Unappropriate application
		new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.PHP_MYADMIN).select();
		new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.PHP_MYADMIN).setChecked(true);
		
		try {
			new WaitUntil(new ShellWithTextIsAvailable("Inappropriate application " + applicationName), 
					TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Shell warning about inappropriate application has not been shown");
		}
		
		new DefaultShell("Inappropriate application " + applicationName);
		
		new NoButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable("Inappropriate application " + applicationName), 
				TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.EDIT_CARTRIDGES);
		
		// Another cartridge required
		new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.ROCK_MONGO).select();
		new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.ROCK_MONGO).setChecked(true);
		
		try {
			new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.ADD_CARTRIDGE_DIALOG), 
					TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Shell offering embedding required cartridge has not been shown");
		}
		
		new DefaultShell(OpenShiftLabel.Shell.ADD_CARTRIDGE_DIALOG);
		
		new PushButton(OpenShiftLabel.Button.APPLY).click();
		
		assertTrue("Cartridge has not been added after confirmation of adding embeddable cartridge.",
				new DefaultTable().getItem(OpenShiftLabel.EmbeddableCartridge.MONGO_DB).isChecked());
	
		new CancelButton().click(); 
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.EDIT_CARTRIDGES), TimePeriod.LONG);
	}
	
	@After
	public void deleteApplication() {
		new DeleteUtils(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN, 
				applicationName, applicationName).perform();
	}
}
