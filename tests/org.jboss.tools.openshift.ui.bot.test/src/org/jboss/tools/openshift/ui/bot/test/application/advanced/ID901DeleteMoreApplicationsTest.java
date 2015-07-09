package org.jboss.tools.openshift.ui.bot.test.application.advanced;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.reddeer.wizard.v2.Templates;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.Datastore;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test capabilities of deleting more applications at once.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID901DeleteMoreApplicationsTest {

	private String firstApplicationName = "fdiy" + System.currentTimeMillis();
	private String secondApplicationName = "sdiy" + System.currentTimeMillis();
	
	@Before
	public void createApplications() {
		new Templates(Datastore.USERNAME, Datastore.DOMAIN, false).createSimpleApplicationOnBasicCartridges(
				OpenShiftLabel.Cartridge.DIY, firstApplicationName, false, true, true);
		new Templates(Datastore.USERNAME, Datastore.DOMAIN, false).createSimpleApplicationOnBasicCartridges(
				OpenShiftLabel.Cartridge.DIY, secondApplicationName, false, true, true);
	}
	
	@Test
	public void testDeleteApplications() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		TreeItem firstApplication = explorer.getApplication(Datastore.USERNAME, Datastore.DOMAIN,
				firstApplicationName);
		TreeItem secondApplication = explorer.getApplication(Datastore.USERNAME, Datastore.DOMAIN,
				secondApplicationName);
		
		new DefaultTree().selectItems(firstApplication, secondApplication);
		
		try {
			new ContextMenu(OpenShiftLabel.ContextMenu.DELETE_APPLICATION).select();
		} catch (SWTLayerException ex) {
			fail("There is no context menu item to delete more application at once.");
		}
		
		try {
			new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.DELETE_APP), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Shell for confirmation of application removal has not been opened.");
		}
		
		new DefaultShell(OpenShiftLabel.Shell.DELETE_APP);	
		new OkButton().click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		AbstractWait.sleep(TimePeriod.getCustom(7));
		
		assertFalse("First application is still presented in OpenShift explorer.",
				explorer.applicationExists(Datastore.USERNAME, Datastore.DOMAIN, firstApplicationName));
		
		assertFalse("Second application is still presented in OpenShift explorer.",
				explorer.applicationExists(Datastore.USERNAME, Datastore.DOMAIN, secondApplicationName));
	}
	
	@After
	public void deleteAdaptersAndProjects() {
		DeleteApplication deleteFirst = new DeleteApplication(Datastore.USERNAME, Datastore.DOMAIN, firstApplicationName);
		DeleteApplication deleteSecond = new DeleteApplication(Datastore.USERNAME, Datastore.DOMAIN, secondApplicationName);
		
		deleteFirst.deleteProject();
		deleteFirst.deleteServerAdapter();
		
		deleteSecond.deleteProject();
		deleteSecond.deleteServerAdapter();
	}
}
