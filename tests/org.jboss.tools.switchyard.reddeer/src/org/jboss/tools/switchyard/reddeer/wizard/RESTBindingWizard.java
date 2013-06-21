package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.util.Bot;

/**
 * SOAP binding.
 * 
 * @author apodhrad
 * 
 */
public class RESTBindingWizard extends WizardDialog {

	public RESTBindingWizard() {
		super();
	}

	public RESTBindingWizard setAddress(String address) {
		// new LabeledText("Address").setText(address);
		Bot.get().textWithLabel("Address").typeText(address);
		return this;
	}

	public RESTBindingWizard addInterface(String javaInterface) {
		new PushButton("Add").click();
		new DefaultText().setText(javaInterface);
		new PushButton("OK").click();
		return this;
	}

}
