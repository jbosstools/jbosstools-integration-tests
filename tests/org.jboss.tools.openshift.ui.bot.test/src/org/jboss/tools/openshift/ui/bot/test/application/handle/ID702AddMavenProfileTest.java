package org.jboss.tools.openshift.ui.bot.test.application.handle;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.YesButton;
import org.jboss.reddeer.swt.impl.ctab.DefaultCTabItem;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.styledtext.DefaultStyledText;
import org.jboss.reddeer.workbench.impl.editor.DefaultEditor;
import org.jboss.tools.openshift.ui.bot.test.application.create.ID414CreateApplicationFromExistingProjectTest;
import org.jboss.tools.openshift.reddeer.utils.OpenShiftLabel;
import org.jboss.tools.openshift.reddeer.utils.v2.Datastore;
import org.jboss.tools.openshift.reddeer.utils.v2.DeleteApplication;
import org.jboss.tools.openshift.reddeer.wizard.v2.NewApplicationWizard;
import org.jboss.tools.openshift.reddeer.wizard.v2.OpenNewApplicationWizard;
import org.junit.After;
import org.junit.Test;

/**
 * Test capabilities of adding OpenShift profile into pom.xml after creating an
 * application.
 *
 * @author mlabuda@redhat.com
 *
 */
public class ID702AddMavenProfileTest {

	private String applicationName = "eap" + System.currentTimeMillis();
	private String projectName = "eapproject" + System.currentTimeMillis();

	@Test
	public void testAdditionOfOpenShiftProfile() {
		ID414CreateApplicationFromExistingProjectTest
				.createJavaEEProject(projectName);

		OpenNewApplicationWizard.openWizardFromExplorer(Datastore.USERNAME,
				Datastore.DOMAIN);
		new NewApplicationWizard().createNewApplicationOnBasicCartridge(
				OpenShiftLabel.Cartridge.JBOSS_EAP, Datastore.DOMAIN,
				applicationName, false, true, false, false, null, null, true,
				projectName, null, null, (String[]) null);

		new WaitUntil(new ShellWithTextIsAvailable(
				OpenShiftLabel.Shell.IMPORT_APPLICATION_WIZARD),
				TimePeriod.VERY_LONG);

		new DefaultShell(OpenShiftLabel.Shell.IMPORT_APPLICATION_WIZARD);
		new OkButton().click();

		new WaitUntil(new ShellWithTextIsAvailable(
				OpenShiftLabel.Shell.ACCEPT_HOST_KEY), TimePeriod.VERY_LONG);

		new DefaultShell(OpenShiftLabel.Shell.ACCEPT_HOST_KEY);
		new YesButton().click();

		new WaitWhile(new ShellWithTextIsAvailable(
				OpenShiftLabel.Shell.NEW_APP_WIZARD), TimePeriod.VERY_LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);

		Project project = new ProjectExplorer().getProject(projectName);

		project.getProjectItem("pom.xml").open();

		new DefaultCTabItem("pom.xml").activate();

		String pomText = new DefaultStyledText().getText();

		assertTrue("Maven profile has not been added into pom.xml",
				pomText.contains("<id>openshift</id>"));
		
		new DefaultEditor().close();
	}
	
	@After
	public void deleteApplication() {
		new DeleteApplication(Datastore.USERNAME, Datastore.DOMAIN, applicationName, projectName).perform();	
	}
}
