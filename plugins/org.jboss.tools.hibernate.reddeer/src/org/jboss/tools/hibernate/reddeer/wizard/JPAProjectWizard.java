package org.jboss.tools.hibernate.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.button.NoButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.WaitUntil;

/**
 * New JPA Project Wizard
 * @author Jiri Peterka
 *
 */
public class JPAProjectWizard extends NewWizardDialog {

	
	public JPAProjectWizard() {
		super("JPA", "JPA Project");
	}
	
	@Override
	public void finish() {	
		super.finish();
		 
		// handle "Open Associated Perspective?" dialog if it occurs
		String title = "Open Associated Perspective?";
		try {
			new WaitUntil(new ShellWithTextIsActive(title));
			new DefaultShell(title);
			new NoButton().click();
		} catch (WaitTimeoutExpiredException e) {
			// do nothing
		}
	}
}
