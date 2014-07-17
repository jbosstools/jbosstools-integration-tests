package org.jboss.tools.jsf.reddeer.ui;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;

public class JSFNewProjectWizard extends NewWizardDialog{
	
	public static final String CATEGORY1="JBoss Tools Web";
	public static final String CATEGORY2="JSF";
	public static final String NAME="JSF Project";
	
	public JSFNewProjectWizard(){
		super(CATEGORY1,CATEGORY2,NAME);
		addWizardPage(new JSFNewProjectFirstPage(), 0);
		addWizardPage(new JSFNewProjectSecondPage(), 1);
	}	
	
	@Override
	public void finish() {

		DefaultShell shell = new DefaultShell();
		Button button = new PushButton("Finish");
		button.click();

		new WaitWhile(new ShellWithTextIsActive(shell.getText()), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	

}
