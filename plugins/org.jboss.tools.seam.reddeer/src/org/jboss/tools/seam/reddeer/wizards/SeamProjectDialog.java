package org.jboss.tools.seam.reddeer.wizards;

import org.eclipse.reddeer.common.wait.TimePeriod;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;
import org.eclipse.reddeer.swt.api.Button;
import org.eclipse.reddeer.swt.condition.ShellIsAvailable;
import org.eclipse.reddeer.swt.impl.button.PushButton;
import org.eclipse.reddeer.swt.impl.shell.DefaultShell;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;

public class SeamProjectDialog extends NewMenuWizard{
	
	public static final String CATEGORY="Seam";
	public static final String NAME="Seam Web Project";
	public static final String SHELL_NAME="Seam Web Project";
	
	public SeamProjectDialog(){
		super(SHELL_NAME,CATEGORY,NAME);
	}
	

	public void finish() {

		DefaultShell shell = new DefaultShell();
		Button button = new PushButton("Finish");
		button.click();

		new WaitWhile(new ShellIsAvailable(shell.getText()), TimePeriod.getCustom(TimePeriod.VERY_LONG.getSeconds()*6));
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	

}
