package org.jboss.tools.openshift.ui.bot.test.application.importing;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.DeleteApplication;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.wizard.application.Templates;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test capabilities of importing an application from OpenShift Explorer view.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID501ImportApplicationViaExplorerTest {

	private String applicationName = "diy" + System.currentTimeMillis();
	
	@Before
	public void createApplicationAndDeleteProject() {
		deleteProjectAndAdapter(applicationName);
	}
	
	public static void deleteProjectAndAdapter(String applicationName) {
		new Templates(Datastore.USERNAME, Datastore.DOMAIN, false).createSimpleApplicationOnBasicCartridges(
				OpenShiftLabel.Cartridge.DIY, applicationName, false, true, true);
		
		DeleteApplication deleteApplication = new DeleteApplication(Datastore.USERNAME,
				Datastore.DOMAIN, applicationName);
		deleteApplication.deleteServerAdapter();
		
		// Workaround for luna issue with deletion of the projects in project explorer
		// workaround some times dont have to work 
		if (!deleteApplication.deleteProject()) {
			new ProjectExplorer().close();
			new ProjectExplorer().open();
			if (!deleteApplication.deleteProject()) {
				fail("Could not delete application, rerun this test.");
			}
		}
	}

	@Test
	public void testImportApplication() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.getApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName).select();
		
		new ContextMenu(OpenShiftLabel.ContextMenu.IMPORT_APPLICATION).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.IMPORT_APPLICATION), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.IMPORT_APPLICATION);
		
		new FinishButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.IMPORT_APPLICATION), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		assertTrue("There is no project in project explorer. Importing was no successfull.",
				new ProjectExplorer().containsProject(applicationName));
	}
	
	@After
	public void deleteApplication() {
		new DeleteApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName).perform();
	}
}
