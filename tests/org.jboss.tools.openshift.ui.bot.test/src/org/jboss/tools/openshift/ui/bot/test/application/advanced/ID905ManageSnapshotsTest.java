package org.jboss.tools.openshift.ui.bot.test.application.advanced;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.api.View;
import org.jboss.tools.openshift.ui.bot.test.application.create.IDXXXCreateTestingApplication;
import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.view.openshift.OpenShiftExplorerView;
import org.junit.Test;

/**
 * Test capabilities of creating various types of application snapshots (binary and full)
 * and storing them in workspace / file system.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID905ManageSnapshotsTest extends IDXXXCreateTestingApplication {
	
	@Test
	public void testManagingSnapshots() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		TreeItem application = explorer.getApplication(Datastore.USERNAME,
				Datastore.DOMAIN, applicationName);
		
		manageSnapshotsTest(explorer, application, applicationName,
				OpenShiftLabel.ContextMenu.SAVE_SNAPSHOT);
	}
	
	public static void manageSnapshotsTest(View viewOfItem, TreeItem itemToHandle, 
			String applicationName, String... contextMenuPath) {
		viewOfItem.open();
		itemToHandle.select();
		
		new ContextMenu(contextMenuPath).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.SAVE_SNAPSHOT),
				TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.SAVE_SNAPSHOT);
		new PushButton(OpenShiftLabel.Button.WORKSPACE).click();
		
		new DefaultShell(OpenShiftLabel.Shell.SELECT_EXISTING_PROJECT);
		new DefaultTable().getItem(applicationName).select();
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.SELECT_EXISTING_PROJECT),
				TimePeriod.NORMAL);
		
		RadioButton fullButton = new RadioButton("Full");
		RadioButton deploymentButton = new RadioButton("Deployment");
		LabeledText destination = new LabeledText(OpenShiftLabel.TextLabels.DESTINATION);
		
		String deploymentFile = applicationName + "-deployment.tar.gz";
		String deployment2File = applicationName + "-deployment(1).tar.gz";
		String fullFile = applicationName + "-full.tar.gz";
		String full2File = applicationName + "-full(1).tar.gz";
		
		// Verify full application storage
		fullButton.click();
		assertTrue("Snapshot file name has not been properly switched to contain full type"
				+ " postfix.", destination.getText().contains(fullFile));
		
		deploymentButton.click();
		assertTrue("Snapshot file name has not been properly switched to contain deployment"
				+ " type in postfix.", destination.getText().contains(deploymentFile));		
		
		// Test click on full again, whether it is switched back
		fullButton.click();
		assertTrue("Snapshot file name has not been properly switched to contain full type"
				+ " postfix.", destination.getText().contains(fullFile));
		
		new FinishButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.SAVE_SNAPSHOT),
				TimePeriod.VERY_LONG);
		
		refreshProject(applicationName);
		assertTrue("There is no full snapshot stored in project folder.",
				new ProjectExplorer().getProject(applicationName).containsItem(fullFile));
		
		viewOfItem.open();
		itemToHandle.select();
		
		new ContextMenu(contextMenuPath).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.SAVE_SNAPSHOT),
				TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.SAVE_SNAPSHOT);
		
		fullButton = new RadioButton("Full");
		deploymentButton = new RadioButton("Deployment");
		destination = new LabeledText(OpenShiftLabel.TextLabels.DESTINATION);
		
		//Verify deployment application storage
		assertTrue("File name should be altered with number in parenthesis, but it is not.",
				destination.getText().contains(full2File));
		
		deploymentButton.click();
		assertTrue("Snapshot file name has not been properly switched to contain deployment"
				+ " type in postfix.", destination.getText().contains(deploymentFile));		
		
		fullButton.click();
		assertTrue("File name should be altered with number in parenthesis, but it is not.",
				destination.getText().contains(full2File));
		
		deploymentButton.click();
		new FinishButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.SAVE_SNAPSHOT),
				TimePeriod.VERY_LONG);
		
		refreshProject(applicationName);
		assertTrue("There is no full snapshot with altered name stored in project folder.",
				new ProjectExplorer().getProject(applicationName).containsItem(deploymentFile));
		
		viewOfItem.open();
		itemToHandle.select();
		
		new ContextMenu(contextMenuPath).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.SAVE_SNAPSHOT),
				TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.SAVE_SNAPSHOT);
		
		fullButton = new RadioButton("Full");
		deploymentButton = new RadioButton("Deployment");
		destination = new LabeledText(OpenShiftLabel.TextLabels.DESTINATION);
		
		// Verify file name alteration
		assertTrue("File name should be altered with number in parenthesis, but it is not.",
				destination.getText().contains(full2File));
		
		deploymentButton.click();
		assertTrue("File name should be altered with number in parenthesis, but it is not.",
				destination.getText().contains(deployment2File));		
		
		fullButton.click();
		assertTrue("File name should be altered with number in parenthesis, but it is not.",
				destination.getText().contains(full2File));
		
		new FinishButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.SAVE_SNAPSHOT),
				TimePeriod.VERY_LONG);
		
		refreshProject(applicationName);
		assertTrue("There is no full snapshot with altered name stored in project folder.",
				new ProjectExplorer().getProject(applicationName).containsItem(full2File));
	}	
	
	private static void refreshProject(String projectName) {
		new ProjectExplorer().getProject(projectName).select();
		new ContextMenu("Refresh").select();
		
		new WaitWhile(new JobIsRunning());
	}
}
