package org.jboss.tools.maven.ui.bot.test.dialog.seam;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;

public class SeamProjectDialog extends NewWizardDialog{
	
	public static final String CATEGORY="Seam";
	public static final String NAME="Seam Web Project";
	
	public SeamProjectDialog(){
		super(CATEGORY,NAME);
		addWizardPage(new SeamProjectFirstPage(), 1);
	}
	
	@Override
	public void finish() {
		log.info("Finish wizard");

		DefaultShell shell = new DefaultShell();
		Button button = new PushButton("Finish");
		checkButtonEnabled(button);
		button.click();

		new WaitWhile(new ShellWithTextIsActive(shell.getText()), TimePeriod.VERY_LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	

}
