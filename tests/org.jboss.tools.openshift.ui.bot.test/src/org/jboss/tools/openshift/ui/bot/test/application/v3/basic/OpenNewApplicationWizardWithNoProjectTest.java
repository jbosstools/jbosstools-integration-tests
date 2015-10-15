package org.jboss.tools.openshift.ui.bot.test.application.v3.basic;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsEnabled;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.reddeer.swt.impl.button.BackButton;
import org.jboss.reddeer.swt.impl.button.CancelButton;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.view.OpenShift3Connection;
import org.jboss.tools.openshift.reddeer.view.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.test.util.DatastoreOS3;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class OpenNewApplicationWizardWithNoProjectTest {

	private String projectName = "tmp-project";
	
	@BeforeClass
	public static void removeProjects() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		OpenShift3Connection connection = explorer.getOpenShift3Connection(
				DatastoreOS3.USERNAME, DatastoreOS3.SERVER);
		connection.getProject(DatastoreOS3.PROJECT1_DISPLAYED_NAME).delete();
		connection.getProject(DatastoreOS3.PROJECT2).delete();
		connection.refresh();
	}
	
	@Test
	public void testOpenNewApplicationWizardViaShellMenuWithNoProjects() {
		new WorkbenchShell().setFocus();
		
		new ShellMenu("File", "New", "Other...").select();
		
		new DefaultShell("New").setFocus();
		
		new DefaultTreeItem("OpenShift", "OpenShift Application").select();
		
		new NextButton().click();
		
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);
		
		for (String comboItem: new DefaultCombo(0).getItems()) {
			if (comboItem.contains(DatastoreOS3.USERNAME) && comboItem.contains(DatastoreOS3.SERVER)) {
				new DefaultCombo(0).setSelection(comboItem);
				break;
			}
		}
		
		new NextButton().click();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CREATE_OS_PROJECT), TimePeriod.LONG);

		new DefaultShell(OpenShiftLabel.Shell.CREATE_OS_PROJECT);
		new LabeledText(OpenShiftLabel.TextLabels.PROJECT_NAME).setText(projectName);
		new FinishButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CREATE_OS_PROJECT), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new WaitUntil(new ButtonWithTextIsEnabled(new BackButton()), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);
		assertTrue("Created project was not preselected for a new OpenShift application",
				new LabeledCombo(OpenShiftLabel.TextLabels.PROJECT).getText().equals(projectName));
		
		closeWizard();
	}
	
	@Test
	public void testOpenNewApplicationWizardFromCentralWithNoProjects() {
		new DefaultToolItem(new WorkbenchShell(), OpenShiftLabel.Others.JBOSS_CENTRAL).click();
		
		new InternalBrowser().execute(OpenShiftLabel.Others.OPENSHIFT_CENTRAL_SCRIPT);
	
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD),
				TimePeriod.LONG);
		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);
		
		for (String comboItem: new DefaultCombo(0).getItems()) {
			if (comboItem.contains(DatastoreOS3.USERNAME) && comboItem.contains(DatastoreOS3.SERVER)) {
				new DefaultCombo(0).setSelection(comboItem);
				break;
			}
		}
		
		new NextButton().click();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CREATE_OS_PROJECT), TimePeriod.LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.CREATE_OS_PROJECT);
		new LabeledText(OpenShiftLabel.TextLabels.PROJECT_NAME).setText(projectName);
		new FinishButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CREATE_OS_PROJECT), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new WaitUntil(new ButtonWithTextIsEnabled(new BackButton()), TimePeriod.LONG);

		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD); 
		assertTrue("Created project was not preselected for a new OpenShift application",
				new LabeledCombo(OpenShiftLabel.TextLabels.PROJECT).getText().equals(projectName));
		
		closeWizard();
	}
	
	@Test
	public void testOpenNewApplicationWizardFromOpenShiftExplorerWithNoProjects() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.reopen();
		
		OpenShift3Connection connection = 
				explorer.getOpenShift3Connection(DatastoreOS3.USERNAME, DatastoreOS3.SERVER);
		connection.select();
		new ContextMenu(OpenShiftLabel.ContextMenu.NEW_OS3_APPLICATION).select();;
		
		try {
			new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CREATE_OS_PROJECT), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Shell to create a new OpenShift application was supposed to be opened. But it's not.");
		}
		
		new DefaultShell(OpenShiftLabel.Shell.CREATE_OS_PROJECT);
		new LabeledText(OpenShiftLabel.TextLabels.PROJECT_NAME).setText(projectName);
		new FinishButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.CREATE_OS_PROJECT), TimePeriod.LONG);
		
		assertTrue("Created project was not preselected for a new OpenShift application",
				new LabeledCombo(OpenShiftLabel.TextLabels.PROJECT).getSelection().equals(projectName));
		
		closeWizard();
	}
	
	private void closeWizard() {
		new CancelButton().click();
		
		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	@After
	public void deleteTmpProject() {
		OpenShift3Connection connection = new OpenShiftExplorerView().getOpenShift3Connection(
				DatastoreOS3.USERNAME, DatastoreOS3.SERVER);
		connection.refresh();
		connection.getProject(projectName).delete();
	}
	
	@AfterClass
	public static void recreateProjects() {
		OpenShiftExplorerView explorer = new OpenShiftExplorerView();
		explorer.open();
		
		OpenShift3Connection connection = explorer.getOpenShift3Connection(
				DatastoreOS3.USERNAME, DatastoreOS3.SERVER);
		connection.createNewProject(DatastoreOS3.PROJECT1, DatastoreOS3.PROJECT1_DISPLAYED_NAME);
		connection.createNewProject(DatastoreOS3.PROJECT2);
	}
}
