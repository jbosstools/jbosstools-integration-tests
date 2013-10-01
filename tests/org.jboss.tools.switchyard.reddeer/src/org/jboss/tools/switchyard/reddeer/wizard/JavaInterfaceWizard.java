package org.jboss.tools.switchyard.reddeer.wizard;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Wizard for creating Java interface.
 * 
 * @author apodhrad
 * 
 */
public class JavaInterfaceWizard extends NewWizardDialog {

	public static final String DIALOG_TITLE = "New Java Interface";
	
	private static SWTWorkbenchBot bot = new SWTWorkbenchBot(); 

	public JavaInterfaceWizard() {
		super("Java", "Interface");
	}

	public JavaInterfaceWizard activate() {
		bot.shell(DIALOG_TITLE).activate();
		return this;
	}

	public JavaInterfaceWizard setName(String name) {
		new LabeledText("Name:").setText(name);
		return this;
	}

}
