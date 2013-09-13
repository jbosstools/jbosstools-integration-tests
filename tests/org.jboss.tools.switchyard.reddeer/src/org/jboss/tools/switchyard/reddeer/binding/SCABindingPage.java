package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * SCA binding page
 * 
 * @author apodhrad
 * 
 */
public class SCABindingPage extends WizardPage {

	public static final String NAME = "Name";
	public static final String CLUSTERED = "Clustered";

	public SCABindingPage setName(String name) {
		new LabeledText(NAME).setFocus();
		new LabeledText(NAME).setText(name);
		return this;
	}

	public String getName() {
		return new LabeledText(NAME).getText();
	}

	public SCABindingPage setClustered(boolean clustered) {
		new CheckBox(CLUSTERED).toggle(clustered);
		return this;
	}

	public boolean isClustered() {
		return new CheckBox(CLUSTERED).isChecked();
	}
}
