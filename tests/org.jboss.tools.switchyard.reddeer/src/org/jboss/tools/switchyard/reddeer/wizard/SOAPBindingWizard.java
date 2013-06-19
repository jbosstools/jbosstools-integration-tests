package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.util.Bot;

/**
 * SOAP binding.
 * 
 * @author apodhrad
 * 
 */
public class SOAPBindingWizard extends WizardDialog {

	public SOAPBindingWizard() {
		super();
	}

	public SOAPBindingWizard setContextpath(String contextPath) {
		// new LabeledText("Context Path").setText(contextPath);
		Bot.get().textWithLabel("Context Path").typeText(contextPath);
		return this;
	}

}
