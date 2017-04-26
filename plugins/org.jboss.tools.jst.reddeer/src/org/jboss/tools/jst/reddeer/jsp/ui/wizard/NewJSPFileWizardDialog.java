package org.jboss.tools.jst.reddeer.jsp.ui.wizard;

import org.jboss.reddeer.eclipse.topmenu.NewMenuWizard;

/**
 * Wizard dialog for creating a JSP File.
 * @author vpakan
 */
public class NewJSPFileWizardDialog extends NewMenuWizard {
	/**
	 * Constructs the wizard with Web > JSP File.
	 */
	public NewJSPFileWizardDialog() {
		super("New JSP File", "Web", "JSP File");
	}
	
}