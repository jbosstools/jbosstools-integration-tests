package org.jboss.tools.jst.reddeer.wst.css.ui.wizard;

import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;

/**
 * Wizard dialog for creating a CSS File.
 * @author vpakan
 */
public class NewCSSWizardDialog extends NewMenuWizard {
	/**
	 * Constructs the wizard with Web > HTML File.
	 */
	public NewCSSWizardDialog() {
		super("New CSS File", "Web", "CSS File");
	}
	
}