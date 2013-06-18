package org.jboss.tools.maven.ui.bot.test.dialog.jsf;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;

public class JSFProjectDialog extends NewWizardDialog{
	
	public static final String CATEGORY1="JBoss Tools Web";
	public static final String CATEGORY2="JSF";
	public static final String NAME="JSF Project";
	
	public JSFProjectDialog(){
		super(CATEGORY1,CATEGORY2,NAME);
		addWizardPage(new JSFProjectFirstPage(), 1);
		addWizardPage(new JSFProjectSecondPage(), 2);
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
