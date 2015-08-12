package org.jboss.tools.ws.reddeer.ui.wizards.wst;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;
import org.jboss.tools.ws.reddeer.ui.wizards.CreateNewFileWizardPage;

/**
 * WSDL File wizard.<br/>
 *
 * The first wizard page is being represented by {@link CreateNewFileWizardPage}.
 * Other wizard pages are not implemented yet.
 *
 * Web Services > WSDL File
 *
 * @author Radoslav Rabara
 *
 */
public class NewWsdlFileWizard extends NewWizardDialog {
	public NewWsdlFileWizard() {
		super("Web Services", "WSDL File");
	}
}
