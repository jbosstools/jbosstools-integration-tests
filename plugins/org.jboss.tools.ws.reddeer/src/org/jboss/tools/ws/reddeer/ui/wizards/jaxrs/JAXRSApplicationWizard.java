package org.jboss.tools.ws.reddeer.ui.wizards.jaxrs;

import org.jboss.reddeer.jface.wizard.NewWizardDialog;

/**
 * JAX-RS Application wizard.
 *
 * Web Services > JAX-RS Application
 *
 * Has only one wizard page - {@link JAXRSApplicationWizardPage}
 *
 * @author Radoslav Rabara
 * @since JBT 4.2.0 Beta2
 * @see http://tools.jboss.org/documentation/whatsnew/jbosstools/4.2.0.Beta2.html#webservices
 */
public class JAXRSApplicationWizard extends NewWizardDialog {
	
	public JAXRSApplicationWizard() {
		super("Web Services", "JAX-RS Application");
	}
}
