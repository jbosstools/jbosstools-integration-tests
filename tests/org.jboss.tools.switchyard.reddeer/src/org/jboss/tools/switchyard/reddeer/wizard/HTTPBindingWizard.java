package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.condition.TableHasRows;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.reddeer.swt.wait.WaitUntil;

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
	
	public HTTPBindingWizard setMessageComposer(String composer) {
		new PushButton("Browse...").click();
		new DefaultText().setText(composer);
		new WaitUntil(new TableHasRows(new DefaultTable()));
		new PushButton("OK").click();
		return this;
	}

}
