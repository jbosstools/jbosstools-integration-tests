package org.jboss.tools.ws.reddeer.ui.wizards.jst.jsp;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.tools.ws.reddeer.ui.wizards.CreateNewFileWizardPage;

/**
 * JSP File wizard.
 *
 * Web > JSP File
 *
 * @author Radoslav Rabara
 *
 */
public class NewJSPFileWizard extends NewWizardDialog {
	public NewJSPFileWizard() {
		super("Web", "JSP File");
		addWizardPage(new CreateNewFileWizardPage(), 0);
	}
}
