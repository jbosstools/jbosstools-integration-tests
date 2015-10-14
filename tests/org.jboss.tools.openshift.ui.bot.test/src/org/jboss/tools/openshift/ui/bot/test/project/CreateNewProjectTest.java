package org.jboss.tools.openshift.ui.bot.test.project;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
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
import org.junit.Test;

public class CreateNewProjectTest {

	@Test
	public void testCreateNewProjectViaManageShell() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.reopen();
		
		OpenShift3Connection connection = explorer.getOpenShift3Connection(DatastoreOS3.USERNAME);
		connection.select();
		new ContextMenu(OpenShiftLabel.ContextMenu.MANAGE_OS_PROJECTS).select();
		
		new DefaultShell(OpenShiftLabel.Shell.MANAGE_OS_PROJECTS);
		new PushButton(OpenShiftLabel.Button.NEW).click();
		
		new DefaultShell(OpenShiftLabel.Shell.CREATE_OS_PROJECT);
		new LabeledText(OpenShiftLabel.TextLabels.PROJECT_NAME).setText(DatastoreOS3.PROJECT1);
		new LabeledText(OpenShiftLabel.TextLabels.PROJECT_DISPLAYED_NAME).setText(DatastoreOS3.PROJECT1_DISPLAYED_NAME);
		new FinishButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CREATE_OS_PROJECT), TimePeriod.LONG);
	
		new DefaultShell(OpenShiftLabel.Shell.MANAGE_OS_PROJECTS);
		try {
			new DefaultTable().getItem(DatastoreOS3.PROJECT1);
		} catch (RedDeerException ex) {
			fail("Project " + DatastoreOS3.PROJECT1 + " does not exist in the table. It has not been created.");
		}
		assertTrue("Displayed name for project " + DatastoreOS3.PROJECT1 + " is not shown in the table.",
				new DefaultTable().getItem(DatastoreOS3.PROJECT1).getText(1).equals(DatastoreOS3.PROJECT1_DISPLAYED_NAME));
		new OkButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.MANAGE_OS_PROJECTS), TimePeriod.LONG);

		try {
			connection.getProject(DatastoreOS3.PROJECT1_DISPLAYED_NAME);
		} catch (RedDeerException ex) {
			fail("OpenShift project created for a connection has not been shown in OpenShift explorer.");
		}
	}
	
	@Test
	public void testCreateNewProjectViaContextMenu() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.reopen();
		
		OpenShift3Connection connection = explorer.getOpenShift3Connection(DatastoreOS3.USERNAME);
		connection.select();
		new ContextMenu(OpenShiftLabel.ContextMenu.NEW_OS_PROJECT).select();
		
		new DefaultShell(OpenShiftLabel.Shell.CREATE_OS_PROJECT);
		new LabeledText(OpenShiftLabel.TextLabels.PROJECT_NAME).setText(DatastoreOS3.PROJECT2);
		new FinishButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CREATE_OS_PROJECT), TimePeriod.LONG);
	
		try {
			connection.getProject(DatastoreOS3.PROJECT2);
		} catch (RedDeerException ex) {
			fail("OpenShift project created for a connection has not been shown in OpenShift explorer.");
		}
	}
}
