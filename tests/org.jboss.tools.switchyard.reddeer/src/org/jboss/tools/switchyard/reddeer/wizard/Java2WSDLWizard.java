package org.jboss.tools.switchyard.reddeer.wizard;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.wait.AbstractWait;

/**
 * Wizard for creating WSDL from Java.
 * 
 * @author apodhrad
 * 
 */
public class Java2WSDLWizard extends NewWizardDialog {

	public static final String DIALOG_TITLE = "Java2WSDL";

	private static SWTWorkbenchBot bot = new SWTWorkbenchBot(); 

	public Java2WSDLWizard() {
		super("SwitchYard", "WSDL File from Java");
	}

	public Java2WSDLWizard activate() {
		bot.shell(DIALOG_TITLE).activate();
		AbstractWait.sleep(1000);
		return this;
	}

	public Java2WSDLWizard openDialog() {
		open();
		return this;
	}
	
	public Java2WSDLWizard nextDialog() {
		next();
		return this;
	}

	@Override
	public void finish() {
		if (!new PushButton("Finish").isEnabled()) {
			System.out.println("Finish is not enabled!!!");
		}
		super.finish();
	}

}
