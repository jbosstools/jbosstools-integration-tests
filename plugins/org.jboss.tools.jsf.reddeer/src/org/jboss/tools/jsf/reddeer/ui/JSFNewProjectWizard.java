package org.jboss.tools.jsf.reddeer.ui;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;

public class JSFNewProjectWizard extends NewWizardDialog{
	
	public static final String CATEGORY1="JBoss Tools Web";
	public static final String CATEGORY2="JSF";
	public static final String NAME="JSF Project";
	
	public JSFNewProjectWizard(){
		super(CATEGORY1,CATEGORY2,NAME);
	}	
	
	@Override
	public void finish() {

		DefaultShell shell = new DefaultShell();
		String shellText = shell.getText();
		Button button = new PushButton("Finish");
		button.click();

		new WaitWhile(new ShellWithTextIsActive(shellText), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	

}
