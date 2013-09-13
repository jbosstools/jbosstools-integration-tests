package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Mail binding page
 * 
 * @author apodhrad
 * 
 */
public class MailBindingPage extends WizardPage {

	public static final String NAME = "Name";
	public static final String HOST = "Host*";

	public MailBindingPage setName(String name) {
		new LabeledText(NAME).setFocus();
		new LabeledText(NAME).setText(name);
		return this;
	}

	public String getName() {
		return new LabeledText(NAME).getText();
	}

	public MailBindingPage setHost(String host) {
		new LabeledText(HOST).setFocus();
		new LabeledText(HOST).setText(host);
		new LabeledText(NAME).setFocus();
		return this;
	}

	public String getHost() {
		return new LabeledText(HOST).getText();
	}
}
