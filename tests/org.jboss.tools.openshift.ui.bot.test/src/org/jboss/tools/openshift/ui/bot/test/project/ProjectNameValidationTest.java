package org.jboss.tools.openshift.ui.bot.test.project;

import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.RedDeerException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShift3Connection;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.test.util.DatastoreOS3;
import org.junit.Test;

public class ProjectNameValidationTest {
	
	@Test
	public void testShortProjectName() {
		openNewProjectShell();
		new LabeledText(OpenShiftLabel.TextLabels.PROJECT_NAME).setText("s");
		
		try {
			new DefaultText(" Project name length must be between 2 and 63 characters.");
		} catch (RedDeerException ex) {
			fail("There is no validation message warning about small length of a project name.");
		}
		
		closeNewProjectShell();
	}
	
	@Test
	public void testInvalidProjectNameFormat() {
		openNewProjectShell();
		new LabeledText(OpenShiftLabel.TextLabels.PROJECT_NAME).setText("--");
		
		try {
			new DefaultText(" A valid project name is alphanumeric (a-z, and 0-9), with the characters -, . allowed anywhere except first or last.");
		} catch (RedDeerException ex) {
			fail("There is no validation message warning about inappropriate project name.");
		}
		
		closeNewProjectShell();
	}
	
	@Test
	public void testForbiddenCharactersInProjectName() {
		openNewProjectShell();
		new LabeledText(OpenShiftLabel.TextLabels.PROJECT_NAME).setText("AAA");
		
		try {
			new DefaultText(" A valid project name is alphanumeric (a-z, and 0-9), with the characters -, . allowed anywhere except first or last.");
		} catch (RedDeerException ex) {
			fail("There is no validation message warning about inappropriate project name.");
		}
		
		closeNewProjectShell();
	}
	
	@Test
	public void testLongProjectName() {
		openNewProjectShell();
		new LabeledText(OpenShiftLabel.TextLabels.PROJECT_NAME).setText("012345678901234567890" +
				"12345678901234567890123456789012345678901234567890123456789012345678901234567890" +
				"123456789012345678901234");
		try {
			new DefaultText(" Maximum length allowed is 63 characters");
		} catch (RedDeerException ex) {
			fail("There is no validation message warning about small length of a project name.");
		}
		
		closeNewProjectShell();
	}
	
	private void openNewProjectShell() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.reopen();
		
		OpenShift3Connection connection = explorer.getOpenShift3Connection(DatastoreOS3.USERNAME);
		connection.select();
		new ContextMenu(OpenShiftLabel.ContextMenu.NEW_OS_PROJECT).select();
		
		new DefaultShell(OpenShiftLabel.Shell.CREATE_OS_PROJECT);
	}
	
	private void closeNewProjectShell() {
		new CancelButton().click();
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CREATE_OS_PROJECT), TimePeriod.LONG);
	}
}
