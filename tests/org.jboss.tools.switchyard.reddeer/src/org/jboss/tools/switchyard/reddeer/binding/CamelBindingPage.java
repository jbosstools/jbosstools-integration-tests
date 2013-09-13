package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Camel binding page
 * 
 * @author apodhrad
 * 
 */
public class CamelBindingPage extends OperationOptionsPage<CamelBindingPage> {

	public static final String NAME = "Name";
	public static final String CONFIG_URI = "Config URI*";

	public CamelBindingPage setName(String name) {
		new LabeledText(NAME).setFocus();
		new LabeledText(NAME).setText(name);
		return this;
	}

	public String getName() {
		return new LabeledText(NAME).getText();
	}

	public CamelBindingPage setConfigURI(String configURI) {
		new LabeledText(CONFIG_URI).setFocus();
		new LabeledText(CONFIG_URI).setText(configURI);
		return this;
	}

	public String getConfigURI() {
		return new LabeledText(CONFIG_URI).getText();
	}
}
