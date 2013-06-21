package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.util.Bot;

/**
 * Wizard for creating Java interface.
 * 
 * @author apodhrad
 * 
 */
public class JavaInterfaceWizard extends NewWizardDialog {

	public static final String DIALOG_TITLE = "New Java Interface";

	public JavaInterfaceWizard() {
		super("Java", "Interface");
	}

	public JavaInterfaceWizard activate() {
		Bot.get().shell(DIALOG_TITLE).activate();
		return this;
	}

	public JavaInterfaceWizard setName(String name) {
		new LabeledText("Name:").setText(name);
		return this;
	}

}
