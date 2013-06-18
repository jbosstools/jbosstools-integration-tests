package org.jboss.tools.maven.ui.bot.test.dialog.maven;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;

public class MavenProjectWizardDialog extends NewWizardDialog{
	
	public static final String CATEGORY="Maven";
	public static final String NAME="Maven Project";
	
	public MavenProjectWizardDialog(){
		super(CATEGORY,NAME);
		addWizardPage(new MavenProjectWizardSecondPage(), 2);
		addWizardPage(new MavenProjectWizardThirdPage(), 3);
	}
	
	@Override
	public void finish() {
		log.info("Finish wizard");

		DefaultShell shell = new DefaultShell();
		Button button = new PushButton("Finish");
		checkButtonEnabled(button);
		button.click();

		new WaitWhile(new ShellWithTextIsActive(shell.getText()), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	

}
