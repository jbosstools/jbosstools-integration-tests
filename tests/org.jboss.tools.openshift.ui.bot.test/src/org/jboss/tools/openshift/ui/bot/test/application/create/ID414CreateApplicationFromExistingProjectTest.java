package org.jboss.tools.openshift.ui.bot.test.application.create;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.wst.server.ui.view.ServersView;
import org.jboss.reddeer.jface.viewer.handler.TreeViewerHandler;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsEnabled;
import org.jboss.reddeer.swt.impl.button.BackButton;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.shell.WorkbenchShell;
import org.jboss.tools.openshift.reddeer.condition.v2.ApplicationIsDeployedSuccessfully;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteUtils;
import org.jboss.tools.openshift.reddeer.wizard.page.v2.FirstWizardPage;
import org.jboss.tools.openshift.reddeer.wizard.page.v2.SecondWizardPage;
import org.jboss.tools.openshift.reddeer.wizard.v2.NewOpenShift2ApplicationWizard;
import org.jboss.tools.openshift.ui.bot.test.util.DatastoreOS2;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test capabilities of deploying a project via Configure menu item.
 * @author mlabuda@redhat.com
 *
 */
public class ID414CreateApplicationFromExistingProjectTest {

	private String applicationName = "eap" + System.currentTimeMillis();

	@Before
	public void provideProject() {
		createJavaEEProject(applicationName);
	}
	
	public static void createJavaEEProject(String applicationName) {
		new WorkbenchShell().setFocus();
		new ShellMenu("File", "New", "Other...").select();

		new DefaultShell("New");
		new DefaultTreeItem("JBoss Central", "Java EE Web Project").select();

		new NextButton().click();

		new WaitUntil(new ShellWithTextIsAvailable("New Project Example"),
				TimePeriod.LONG);

		new DefaultShell("New Project Example");

		new NextButton().click();

		new LabeledCombo("Project name").setText(applicationName);

		new FinishButton().click();

		new WaitWhile(new ShellWithTextIsAvailable("New Project Example"),
				TimePeriod.LONG);

		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

		new WaitUntil(new ShellWithTextIsAvailable("New Project Example"),
				TimePeriod.LONG);

		new DefaultShell("New Project Example");
		new CheckBox(1).click();
		new FinishButton().click();

		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	@Test
	public void testDeployExistingProjectViaConfigureMenu() {
		TreeViewerHandler treeViewerHandler = TreeViewerHandler.getInstance();
		ProjectExplorer projectExplorer = new ProjectExplorer();
		projectExplorer.getProject(applicationName).select();

		new ContextMenu(OpenShiftLabel.ContextMenu.DEPLOY_PROJECT).select();
		
		new WaitUntil(new ShellWithTextIsAvailable(
				OpenShiftLabel.Shell.NEW_APP_WIZARD), TimePeriod.LONG);

		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD);

		for (String comboItem : new DefaultCombo(0).getItems()) {
			if (comboItem.contains(DatastoreOS2.USERNAME)) {
				new DefaultCombo(0).setSelection(comboItem);
				break;
			}
		}

		new NextButton().click();

		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		// to be sure, it is processed
		new WaitUntil(new ButtonWithTextIsEnabled(new BackButton()),
				TimePeriod.LONG);

		new DefaultShell(OpenShiftLabel.Shell.NEW_APP_WIZARD).setFocus();

		NewOpenShift2ApplicationWizard wizard = new NewOpenShift2ApplicationWizard(DatastoreOS2.USERNAME, DatastoreOS2.SERVER,
				DatastoreOS2.DOMAIN);
		
		FirstWizardPage first = new FirstWizardPage();
		first.createNewApplicationOnBasicCartridge(OpenShiftLabel.Cartridge.JBOSS_EAP);

		wizard.next();

		SecondWizardPage secondPage = new SecondWizardPage();
		secondPage.fillApplicationDetails(DatastoreOS2.DOMAIN, applicationName,
				false, true, false, null);

		wizard.next();

		assertTrue(
				"Selected project has not been selected in wizard.",
				!new CheckBox(0).isChecked()
						&& applicationName.equals(new LabeledText(
								"Use existing project:").getText()));

		wizard.finish();

		new WaitUntil(new ShellWithTextIsAvailable(
				OpenShiftLabel.Shell.IMPORT_APPLICATION_WIZARD), TimePeriod.VERY_LONG);

		new DefaultShell(OpenShiftLabel.Shell.IMPORT_APPLICATION_WIZARD);
		new OkButton().click();

		new WaitUntil(new ShellWithTextIsAvailable(
				OpenShiftLabel.Shell.ACCEPT_HOST_KEY), TimePeriod.VERY_LONG);

		new DefaultShell(OpenShiftLabel.Shell.ACCEPT_HOST_KEY);
		new YesButton().click();

		new WaitWhile(new ShellWithTextIsAvailable(
				OpenShiftLabel.Shell.NEW_APP_WIZARD), TimePeriod.VERY_LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);

		ServersView servers = new ServersView();
		servers.open();

		treeViewerHandler.getTreeItem(new DefaultTree(),
				applicationName + " at OpenShift").select();
		new ContextMenu("Publish").select();

		new DefaultShell("Publish " + applicationName + "?");

		new YesButton().click();

		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		
		try {
			new WaitUntil(new ApplicationIsDeployedSuccessfully(DatastoreOS2.USERNAME, DatastoreOS2.SERVER,
					DatastoreOS2.DOMAIN, applicationName, "Welcome to WildFly"), TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException ex) {
			fail("Application has not been deployed successfully.");
		}
	}

	@After
	public void deleteApplication() {
		new DeleteUtils(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.DOMAIN,
				applicationName, applicationName).perform();
	}
}
