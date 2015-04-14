package org.jboss.tools.maven.reddeer.wizards;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitWhile;

public class MavenProjectWizard extends NewWizardDialog{
	
	public static final String CATEGORY="Maven";
	public static final String NAME="Maven Project";
	
	public MavenProjectWizard(){
		super(CATEGORY,NAME);
		addWizardPage(new MavenProjectWizardSecondPage(), 1);
		addWizardPage(new MavenProjectWizardThirdPage(), 2);
	}
	
	@Override
	public void finish() {
		String shell = new DefaultShell().getText();
		Button button = new PushButton("Finish");
		button.click();

		new WaitWhile(new ShellWithTextIsActive(shell), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}
	
	

}
