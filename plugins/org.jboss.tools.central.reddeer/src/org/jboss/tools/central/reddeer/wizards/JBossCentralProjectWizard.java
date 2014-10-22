package org.jboss.tools.central.reddeer.wizards;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.shell.WorkbenchShell;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.uiforms.impl.hyperlink.DefaultHyperlink;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.central.reddeer.projects.ArchetypeProject;
import org.jboss.tools.maven.reddeer.project.examples.wizard.ArchetypeExamplesWizardFirstPage;
import org.jboss.tools.maven.reddeer.project.examples.wizard.ArchetypeExamplesWizardPage;
import org.jboss.tools.maven.reddeer.project.examples.wizard.NewProjectExamplesStacksRequirementsPage;

public class JBossCentralProjectWizard extends WizardDialog {

	protected final static Logger log = Logger
			.getLogger(JBossCentralProjectWizard.class);
	private ArchetypeProject project;

	public JBossCentralProjectWizard(ArchetypeProject project) {
		this.project = project;
		addWizardPage(new NewProjectExamplesStacksRequirementsPage(), 0);
		addWizardPage(new ArchetypeExamplesWizardFirstPage(), 1);
		addWizardPage(new ArchetypeExamplesWizardPage(), 2);
	}

	public void open() {
		new DefaultToolItem(new WorkbenchShell(), "JBoss Central").click();
		DefaultSection startSection = new DefaultSection("Start from scratch");
		new DefaultHyperlink(startSection, project.getName()).activate();
		new DefaultShell("New Project Example");
	}

	/**
	 * Finishes this wizard and returns {@link NewProjectExamplesReadyPage} for
	 * user to validate that shell (for example for opening of cheatsheet).
	 * 
	 */

	public NewProjectExamplesReadyPage finishAndWait() {
		log.info("Finish example wizard");

		DefaultShell shell = new DefaultShell();
		Button button = new PushButton("Finish");
		button.click();

		new WaitWhile(new ShellWithTextIsActive(shell.getText()),
				TimePeriod.getCustom(1200));
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		return new NewProjectExamplesReadyPage(project);
	}

	/**
	 * Finishes this wizard without posibility to check
	 * NewProjectExamplesReadyPage.
	 */

	public void finish() {
		NewProjectExamplesReadyPage projectReadyPage = finishAndWait();
		projectReadyPage.finish();
	}

}
