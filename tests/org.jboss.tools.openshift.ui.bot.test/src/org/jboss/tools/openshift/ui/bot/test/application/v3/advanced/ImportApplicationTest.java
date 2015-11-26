package org.jboss.tools.openshift.ui.bot.test.application.v3.advanced;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.TreeHasChildren;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.openshift.reddeer.condition.ButtonWithTextIsAvailable;
import org.jboss.tools.openshift.reddeer.condition.ResourceExists;
import org.jboss.tools.openshift.reddeer.enums.Resource;
import org.jboss.tools.openshift.reddeer.utils.DatastoreOS3;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.TestUtils;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.test.application.v3.create.AbstractCreateApplicationTest;
import org.junit.Before;
import org.junit.Test;

public class ImportApplicationTest extends AbstractCreateApplicationTest {

	@Before
	public void cleanGitFolder() {
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		
		if (projectExplorer.containsProject(projectName)) {
			projectExplorer.getProject(projectName).delete(true);
		}
		
		TestUtils.cleanupGitFolder(gitFolder);
	}
	
	@Test
	public void testImportOpenShiftApplicationViaOpenShiftExplorer() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		new WaitUntil(new ResourceExists(Resource.BUILD_CONFIG), TimePeriod.LONG);
		
		explorer.getOpenShift3Connection().getProject().getOpenShiftResources(Resource.BUILD_CONFIG).get(0).select();
		new ContextMenu(OpenShiftLabel.ContextMenu.IMPORT_APPLICATION).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.IMPORT_APPLICATION), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.IMPORT_APPLICATION);
		TreeItem buildConfigItem = new DefaultTreeItem(DatastoreOS3.PROJECT1_DISPLAYED_NAME + " " + 
				DatastoreOS3.PROJECT1, "eap-app https://github.com/jboss-developer/jboss-eap-quickstarts");
		
		assertTrue("Selected build config from OpenShift Explorer should be preselected to import an application",
				buildConfigItem.isSelected());
		
		new NextButton().click();
		
		new FinishButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.IMPORT_APPLICATION), TimePeriod.VERY_LONG);
		
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		assertTrue("There should be imported kitchen sink project, but there is not",
				projectExplorer.containsProject(projectName));
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	@Test
	public void testImportOpenShiftApplicationViaShellMenu() {
		new ShellMenu("File", "Import...").select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.IMPORT), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.IMPORT);
		new DefaultTreeItem("OpenShift", "Existing OpenShift Application").select();
		new NextButton().click();
		
		new DefaultShell(OpenShiftLabel.Shell.IMPORT_APPLICATION);
		
		new NextButton().click();

		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
			
		new WaitUntil(new ButtonWithTextIsAvailable("Refresh"), TimePeriod.LONG);
		new WaitUntil(new TreeHasChildren(new DefaultTree()), TimePeriod.NORMAL);
		
		new DefaultTreeItem(DatastoreOS3.PROJECT1_DISPLAYED_NAME + " " + DatastoreOS3.PROJECT1, 
				"eap-app https://github.com/jboss-developer/jboss-eap-quickstarts").select();
		
		new WaitUntil(new WidgetIsEnabled(new NextButton()), TimePeriod.NORMAL);
		
		new NextButton().click();
		new FinishButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.IMPORT_APPLICATION), TimePeriod.VERY_LONG);
		
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.open();
		assertTrue("There should be imported kitchen sink project, but there is not",
				projectExplorer.containsProject(projectName));
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
}
