package org.jboss.tools.switchyard.reddeer.binding;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.tools.switchyard.reddeer.widget.RadioButton;

public abstract class OperationOptionsPage<T> extends WizardPage {

	public static final String OPERATION_NAME = "Operation Name";
	public static final String XPATH = "XPath";
	public static final String REGEX = "Regex";
	public static final String JAVA_CLASS = "Java Class";

	@SuppressWarnings("unchecked")
	public T setOperation(String operation) {
		new RadioButton(OPERATION_NAME).click();
		new SWTWorkbenchBot().comboBox(0).setFocus();
		new DefaultCombo(0).setSelection(operation);
		return (T) this;
	}

	public String getOperation() {
		return new DefaultCombo(0).getText();
	}

}
