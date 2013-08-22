package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.util.Bot;

/**
 * HTTP binding.
 * 
 * @author apodhrad
 * 
 */
public class HTTPBindingWizard extends WizardDialog {

	public static final String DIALOG_TITLE = "HTTP Binding";

	public HTTPBindingWizard() {
		super();
	}

	public HTTPBindingWizard activate() {
		Bot.get().shell(DIALOG_TITLE).activate();
		return this;
	}

	public HTTPBindingWizard setContextpath(String contextPath) {
		Bot.get().textWithLabel("Context Path").setFocus();
		new LabeledText("Context Path").setText(contextPath);
		return this;
	}

	public HTTPBindingWizard setOperation(String operation) {
		Bot.get().comboBox(0).setFocus();
		Bot.get().comboBox(0).setSelection(operation);
		return this;
	}

}
