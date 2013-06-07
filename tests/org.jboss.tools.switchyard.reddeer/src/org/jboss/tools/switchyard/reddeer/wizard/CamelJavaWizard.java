package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;

/**
 * 
 * @author apodhrad
 * 
 */
public class CamelJavaWizard extends ServiceWizard {

	public CamelJavaWizard() {
		super();
	}

	public CamelJavaWizard setName(String name) {
		new LabeledText("Name:").setText(name);
		return this;
	}

	public CamelJavaWizard open() {
		new SwitchYardEditor().addComponent("Camel (Java)");
		return this;
	}

	@Override
	protected void browse() {
		new PushButton(2).click();
	}

}
