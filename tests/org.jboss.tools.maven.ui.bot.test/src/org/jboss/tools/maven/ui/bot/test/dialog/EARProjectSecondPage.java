package org.jboss.tools.maven.ui.bot.test.dialog;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.maven.ui.bot.test.utils.ButtonIsEnabled;

public class EARProjectSecondPage extends WizardPage{
	
	public void addModules(){
		new PushButton("New Module...").click();
		new DefaultShell("Create default Java EE modules.");
		new WaitUntil(new ButtonIsEnabled("Finish"));
		new PushButton("Finish").click();
		new WaitWhile(new JobIsRunning());
		new DefaultShell("New EAR Application Project");
	}
	
	

}
