package org.jboss.tools.openshift.ui.bot.test.application.advanced;

import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.button.NextButton;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.openshift.ui.utils.Datastore;
import org.jboss.tools.openshift.ui.utils.DeleteApplication;
import org.jboss.tools.openshift.ui.utils.OpenShiftLabel;
import org.jboss.tools.openshift.ui.utils.TestUtils;
import org.jboss.tools.openshift.ui.wizard.application.NewApplicationWizard;
import org.jboss.tools.openshift.ui.wizard.application.OpenNewApplicationWizard;
import org.junit.After;
import org.junit.Test;

/**
 * Test capabilities of deploying a github based project.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID902DeployGitProjectTest {

	private String projectName = "jboss-javaee6-webapp";
	private String applicationName = "jbosseapapp" + System.currentTimeMillis();
	private String gitProjectURI = "https://github.com/mlabuda/jboss-eap-application.git";
	
	@Test
	public void testDeployGitBasedProject() {
		TestUtils.cleanupGitFolder("jboss-eap-application");
		new WorkbenchShell().setFocus();
		
		importGitProject();
		
		OpenNewApplicationWizard.openWizardFromExplorer(Datastore.USERNAME, Datastore.DOMAIN);
		
		NewApplicationWizard wizard = new NewApplicationWizard();
		wizard.createNewApplicationOnBasicCartridge(OpenShiftLabel.Cartridge.JBOSS_EAP, 
				Datastore.DOMAIN, applicationName, false, true, false,
				false, null, null, true, projectName, null, "openshift22", (String[]) null);
		
		new WaitUntil(new ShellWithTextIsAvailable(
				OpenShiftLabel.Shell.IMPORT_APPLICATION_WIZARD),
				TimePeriod.VERY_LONG);

		new DefaultShell(OpenShiftLabel.Shell.IMPORT_APPLICATION_WIZARD);
		new OkButton().click();
		
		new WaitUntil(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.ACCEPT_HOST_KEY), TimePeriod.VERY_LONG);
		
		new DefaultShell(OpenShiftLabel.Shell.ACCEPT_HOST_KEY);
		
		new YesButton().click();

		new WaitWhile(new ShellWithTextIsAvailable(OpenShiftLabel.Shell.NEW_APP_WIZARD),
				TimePeriod.VERY_LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		
		wizard.verifyApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName,
				projectName);
	}
	
	private void importGitProject() {
		new ShellMenu("File", "Import...").select();
		
		new WaitUntil(new ShellWithTextIsAvailable("Import"), TimePeriod.LONG);
		
		new DefaultShell("Import").setFocus();
		new DefaultTreeItem("Git", "Projects from Git").select();
		
		new WaitUntil(new ButtonWithTextIsActive(new NextButton()), TimePeriod.NORMAL);
		new NextButton().click();
		
		new DefaultTree().selectItems(new DefaultTreeItem("Clone URI")); 
		
		new WaitUntil(new ButtonWithTextIsActive(new NextButton()), TimePeriod.NORMAL);
		new NextButton().click();
		
		new LabeledText("URI:").setText(gitProjectURI);
		
		new WaitUntil(new ButtonWithTextIsActive(new NextButton()), TimePeriod.NORMAL);
		new NextButton().click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

		new WaitUntil(new ButtonWithTextIsActive(new NextButton()), TimePeriod.NORMAL);
		new NextButton().click();
		
		new WaitUntil(new ButtonWithTextIsActive(new NextButton()), TimePeriod.NORMAL);
		new NextButton().click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		
		new WaitUntil(new ButtonWithTextIsActive(new NextButton()), TimePeriod.NORMAL);
		new NextButton().click();
		
		new WaitUntil(new ButtonWithTextIsActive(new FinishButton()), TimePeriod.LONG);
		new FinishButton().click();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	@After
	public void deleteApplication() {
		new DeleteApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName, projectName).perform();
	}
}
