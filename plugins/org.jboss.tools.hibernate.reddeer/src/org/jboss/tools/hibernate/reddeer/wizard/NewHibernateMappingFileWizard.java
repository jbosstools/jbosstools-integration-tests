package org.jboss.tools.hibernate.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.button.NoButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.WaitUntil;

/**
 * New Hibernate xml mapping file wizard
 * @author Jiri Peterka
 *
 */
public class NewHibernateMappingFileWizard extends NewWizardDialog {

	/**
	 * Initializes wizard
	 */
	public NewHibernateMappingFileWizard() {
		super("Hibernate", "Hibernate XML Mapping file (hbm.xml)");
	}
	
	/**
	 * Finishes hbm xml file wizard 
	 */
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
