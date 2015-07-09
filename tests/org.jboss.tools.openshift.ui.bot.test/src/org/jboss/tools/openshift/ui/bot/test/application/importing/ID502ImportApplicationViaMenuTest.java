package org.jboss.tools.openshift.ui.bot.test.application.importing;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsEnabled;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.Datastore;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test capabilities of importing an application via menu File - Import.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID502ImportApplicationViaMenuTest {

	private String applicationName = "diy" + System.currentTimeMillis();
	
	@Before
	public void createApplicationDeleteProject() {
		ID501ImportApplicationViaExplorerTest.deleteProjectAndAdapter(applicationName);
	}
	
	@Test
	public void testImportApplicationViaMenu() {
		TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
		new WorkbenchShell().setFocus();
		new ShellMenu("File", "Import...").select();
		
		new DefaultShell("Import");
		
		new DefaultTreeItem("OpenShift", "Existing OpenShift Application").select();

		new NextButton().click();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.IMPORT_APPLICATION));
		new DefaultShell(OpenShiftLabel.Shell.IMPORT_APPLICATION);
		
		for (String comboItem: new DefaultCombo(0).getItems()) {
			if (comboItem.contains(Datastore.USERNAME)) {
				new DefaultCombo(0).setSelection(comboItem);
				break;
			}
		}
		
		new NextButton().click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.IMPORT_APPLICATION), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.IMPORT_APPLICATION);
		
		new PushButton(OpenShiftLabel.Button.BROWSE).click();
		
		new DefaultShell(OpenShiftLabel.Shell.SELECT_EXISTING_APPLICATION);
		
		treeViewerHandler.getTreeItem(new DefaultTree(), Datastore.DOMAIN, applicationName).select();
		
		new WaitUntil(new ButtonWithTextIsEnabled(new OkButton()), TimePeriod.LONG);
		
		new OkButton().click();
		
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
