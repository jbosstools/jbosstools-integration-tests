package org.jboss.tools.openshift.ui.bot.test.project;

import static org.junit.Assert.assertFalse;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShift3Connection;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.test.util.DatastoreOS3;
import org.junit.After;
import org.junit.Test;

public class DeleteProjectTest {

	private boolean projectExists;
	
	@Test
	public void deleteProjectViaContextMenuTest() {
		projectExists = true;
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		OpenShift3Connection connection = explorer.getOpenShift3Connection(DatastoreOS3.USERNAME, DatastoreOS3.SERVER);
		connection.getProject(DatastoreOS3.PROJECT1_DISPLAYED_NAME).select();;
		new ContextMenu(OpenShiftLabel.ContextMenu.DELETE_OS_PROJECT).select();
		
		new DefaultShell(OpenShiftLabel.Shell.DELETE_OS_PROJECT);
		new OkButton().click();
		
		projectExists = false;
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.DELETE_OS_PROJECT), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		assertFalse("Project is still presented in OpenShift explorer under a connection.", 
				connection.projectExists(DatastoreOS3.PROJECT1_DISPLAYED_NAME));
	}
	
	@Test
	public void deleteProjectViaManageProjectsShellTest() {
		projectExists = true;
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		OpenShift3Connection connection = explorer.getOpenShift3Connection(DatastoreOS3.USERNAME, DatastoreOS3.SERVER);
		connection.select();
		new ContextMenu(OpenShiftLabel.ContextMenu.MANAGE_OS_PROJECTS).select();
		
		new DefaultShell(OpenShiftLabel.Shell.MANAGE_OS_PROJECTS);
		new DefaultTable().getItem(DatastoreOS3.PROJECT1).select();
		new PushButton(OpenShiftLabel.Button.REMOVE).click();
		
		new DefaultShell(OpenShiftLabel.Shell.DELETE_OS_PROJECT);
		new OkButton().click();
		
		projectExists = false;
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.DELETE_OS_PROJECT), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		assertFalse("There should not be present project in the table.", 
				new DefaultTable().containsItem(DatastoreOS3.PROJECT1));
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.MANAGE_OS_PROJECTS), TimePeriod.LONG);
		
		assertFalse("Project is still presented in OpenShift explorer under a connection.", 
				connection.projectExists(DatastoreOS3.PROJECT1_DISPLAYED_NAME));
	}
	
	@After
	public void recreateProject() {
		if (!projectExists) {
			OpenShiftExplorerView explorer = new OpenShiftExplorerView();
			explorer.reopen();
			
			explorer.getOpenShift3Connection(DatastoreOS3.USERNAME).select();
			new ContextMenu(OpenShiftLabel.ContextMenu.NEW_OS_PROJECT).select();
			
			new DefaultShell(OpenShiftLabel.Shell.CREATE_OS_PROJECT);
			new LabeledText(OpenShiftLabel.TextLabels.PROJECT_NAME).setText(DatastoreOS3.PROJECT1);
			new LabeledText(OpenShiftLabel.TextLabels.PROJECT_DISPLAYED_NAME).setText(
					DatastoreOS3.PROJECT1_DISPLAYED_NAME);
			new FinishButton().click();
			
			new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CREATE_OS_PROJECT), TimePeriod.LONG);
		}
	}
}
