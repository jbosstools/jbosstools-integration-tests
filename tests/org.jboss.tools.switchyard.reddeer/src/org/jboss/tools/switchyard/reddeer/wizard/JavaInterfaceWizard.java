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

	private String name;

	public JavaInterfaceWizard(String name) {
		super("Java", "Interface");
		this.name = name;
	}

	@Override
	public void finish() {
		Bot.get().shell(DIALOG_TITLE).activate();
		new LabeledText("Name:").setText(name);
		super.finish();
	}

}
