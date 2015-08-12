package org.jboss.tools.seam.reddeer.wizards;

import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;

public class SeamProjectDialog extends NewWizardDialog{
	
	public static final String CATEGORY="Seam";
	public static final String NAME="Seam Web Project";
	
	public SeamProjectDialog(){
		super(CATEGORY,NAME);
	}
	
	@Override
	public void finish() {

		DefaultShell shell = new DefaultShell();
		Button button = new PushButton("Finish");
		button.click();

		new WaitWhile(new ShellWithTextIsAvailable(shell.getText()), TimePeriod.getCustom(TimePeriod.VERY_LONG.getSeconds()*6));
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	

}
