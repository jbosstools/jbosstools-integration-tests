package org.jboss.tools.central.reddeer.wizards;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.central.reddeer.exception.CentralException;
import org.jboss.tools.maven.reddeer.project.examples.wizard.MavenExamplesRequirementPage;

public class NewProjectExamplesWizardDialogCentral extends WizardDialog {

	public NewProjectExamplesWizardDialogCentral() {
		addWizardPage(new MavenExamplesRequirementPage(), 0);
		addWizardPage(new NewProjectExamplesLocationPage(), 1);
	}
	
	public void finish(String projectName) {
		log.info("Finish wizard");

		String shellText = new DefaultShell().getText();
		Button button = new PushButton("Finish");
		button.click();

		new WaitWhile(new ShellWithTextIsActive(shellText), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		new DefaultShell("New Project Example");
		CheckBox quickFix = new CheckBox("Show the Quick Fix dialog");
		if (quickFix.isEnabled()){
			throw new CentralException("Quick Fix should not be enabled.");
		}
		if (new CheckBox(1).isEnabled()) {
			assertTrue(new CheckBox(1).isChecked());
		}
		assertTrue(new CheckBox("Do not show this page again").isEnabled());
		assertFalse(new CheckBox("Do not show this page again").isChecked());
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsActive("New Project Example"));
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
}
