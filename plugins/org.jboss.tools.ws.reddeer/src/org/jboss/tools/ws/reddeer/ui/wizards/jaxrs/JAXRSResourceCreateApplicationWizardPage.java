package org.jboss.tools.ws.reddeer.ui.wizards.jaxrs;

import org.jboss.reddeer.swt.impl.button.RadioButton;

/**
 * {@link JAXRSResourceWizard}'s second wizard page
 *
 * Extends {@link JAXRSApplicationWizardPage} with another option:
 * - "Skip the JAX-RS Application creation" - {@link #useSkipTheJAXRSApplicationCreation()}
 *
 * @author Radoslav Rabara
 *
 */
public class JAXRSResourceCreateApplicationWizardPage extends JAXRSApplicationWizardPage {
	public void useSkipTheJAXRSApplicationCreation() {
		new RadioButton(2).click();//"Skip the JAX-RS Application creation"
	}
}
