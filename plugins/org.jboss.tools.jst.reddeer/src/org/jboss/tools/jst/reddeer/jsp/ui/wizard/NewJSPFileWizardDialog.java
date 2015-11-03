package org.jboss.tools.jst.reddeer.jsp.ui.wizard;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;

/**
 * Wizard dialog for creating a JSP File.
 * @author vpakan
 */
public class NewJSPFileWizardDialog extends NewWizardDialog {
	/**
	 * Constructs the wizard with Web > JSP File.
	 */
	public NewJSPFileWizardDialog() {
		super("Web", "JSP File");
	}
	
}