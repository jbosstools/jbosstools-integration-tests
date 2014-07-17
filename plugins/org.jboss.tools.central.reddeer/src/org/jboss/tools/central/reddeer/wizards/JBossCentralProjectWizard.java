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
import org.jboss.reddeer.swt.impl.toolbar.WorkbenchToolItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.uiforms.impl.hyperlink.DefaultHyperlink;
import org.jboss.reddeer.uiforms.impl.section.DefaultSection;
import org.jboss.tools.maven.reddeer.project.examples.wizard.ArchetypeExamplesWizardFirstPage;
import org.jboss.tools.maven.reddeer.project.examples.wizard.ArchetypeExamplesWizardPage;
import org.jboss.tools.maven.reddeer.project.examples.wizard.NewProjectExamplesStacksRequirementsPage;


public class JBossCentralProjectWizard extends WizardDialog{
	
	protected final static Logger log = Logger.getLogger(JBossCentralProjectWizard.class);

	public JBossCentralProjectWizard(){
		addWizardPage(new NewProjectExamplesStacksRequirementsPage(), 0);
		addWizardPage(new ArchetypeExamplesWizardFirstPage(), 1);
		addWizardPage(new ArchetypeExamplesWizardPage(), 2);
	}
	
	public void open(String projectExampleName){
		new WorkbenchToolItem("JBoss Central").click();
		DefaultSection startSection = new DefaultSection("Start from scratch");
		new DefaultHyperlink(startSection, projectExampleName).activate();
		new DefaultShell("New Project Example");
	}
	
	public void finish(String projectName){
		finish(projectName, false);
	}
	
	
	/**
	 * Finishes this wizard and waits for a long time.  
	 * 
	 * @param projectName
	 * @param empty true if it's empty project. It means that readme is not demanded (empty project does not have readme).
	 */
	
	public void finish(String projectName, boolean empty){
		log.info("Finish example wizard");

		DefaultShell shell = new DefaultShell();
		Button button = new PushButton("Finish");
		button.click();

		new WaitWhile(new ShellWithTextIsActive(shell.getText()), TimePeriod.getCustom(1200));
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		new DefaultShell("New Project Example");
		assertFalse(new CheckBox("Show the Quick Fix dialog").isEnabled());
		if (!empty){
			assertTrue(new CheckBox("Show '/"+projectName+"/README.md' for further instructions").isEnabled());
			assertTrue(new CheckBox("Show '/"+projectName+"/README.md' for further instructions").isChecked());
		}
		assertTrue(new CheckBox("Do not show this page again").isEnabled());
		assertFalse(new CheckBox("Do not show this page again").isChecked());
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsActive("New Project Example"));
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	
	
	

}
