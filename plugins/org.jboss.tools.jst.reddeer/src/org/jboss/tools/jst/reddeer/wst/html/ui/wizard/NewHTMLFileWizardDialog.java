package org.jboss.tools.jst.reddeer.wst.html.ui.wizard;

import org.eclipse.reddeer.eclipse.selectionwizard.NewMenuWizard;

/**
 * Wizard dialog for creating a HTML File.
 * @author vpakan
 */
public class NewHTMLFileWizardDialog extends NewMenuWizard {
	/**
	 * Constructs the wizard with Web > HTML File.
	 */
	public NewHTMLFileWizardDialog() {
		super("New HTML File", "Web", "HTML File");
	}
	
}