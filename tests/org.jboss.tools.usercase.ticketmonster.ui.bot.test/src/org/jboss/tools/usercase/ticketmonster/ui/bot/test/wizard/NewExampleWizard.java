package org.jboss.tools.usercase.ticketmonster.ui.bot.test.wizard;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
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

public class NewExampleWizard extends WizardDialog{
	
	public NewExampleWizard(){
		addWizardPage(new NewExampleWizardFirstPage(), 0);
		addWizardPage(new NewExampleWizardSecondPage(), 1);
		addWizardPage(new NewExampleWizardThirdPage(), 2);
	}
	
	public void open(String projectExampleName){
		new WorkbenchToolItem("JBoss Central").click();
		DefaultSection startSection = new DefaultSection("Start from scratch");
		new DefaultHyperlink(startSection, projectExampleName).activate();
		new DefaultShell("New Project Example");
	}
	
	public void finish(String projectName){
		log.info("Finish example wizard");

		DefaultShell shell = new DefaultShell();
		Button button = new PushButton("Finish");
		button.click();

		new WaitWhile(new ShellWithTextIsActive(shell.getText()), TimePeriod.getCustom(1200));
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
		new DefaultShell("New Project Example");
		assertFalse(new CheckBox("Show the Quick Fix dialog").isEnabled());
		assertTrue(new CheckBox("Show '/"+projectName+"/README.md' for further instructions").isEnabled());
		assertTrue(new CheckBox("Show '/"+projectName+"/README.md' for further instructions").isChecked());
		assertTrue(new CheckBox("Do not show this page again").isEnabled());
		assertFalse(new CheckBox("Do not show this page again").isChecked());
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsActive("New Project Example"));
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	
	
	

}
