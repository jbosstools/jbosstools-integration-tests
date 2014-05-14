package org.jboss.tools.seam.reddeer.wizards;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;

public class SeamProjectDialog extends NewWizardDialog{
	
	public static final String CATEGORY="Seam";
	public static final String NAME="Seam Web Project";
	
	public SeamProjectDialog(){
		super(CATEGORY,NAME);
		addWizardPage(new SeamProjectFirstPage(), 0);
		addWizardPage(new SeamProjectFifthPage(), 5);
	}
	
	@Override
	public void finish() {

		DefaultShell shell = new DefaultShell();
		Button button = new PushButton("Finish");
		button.click();

		new WaitWhile(new ShellWithTextIsAvailable(shell.getText()), TimePeriod.getCustom(TimePeriod.VERY_LONG.getSeconds()*4));
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	

}
