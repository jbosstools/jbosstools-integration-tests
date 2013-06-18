package org.jboss.tools.maven.ui.bot.test.dialog;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;

public class EARProjectSecondPage extends WizardPage{
	
	public void addModules(){
		new PushButton("New Module...").click();
		new DefaultShell("Create default Java EE modules.");
		new PushButton("Finish").click();
		new DefaultShell("New EAR Application Project");
	}
	
	

}
