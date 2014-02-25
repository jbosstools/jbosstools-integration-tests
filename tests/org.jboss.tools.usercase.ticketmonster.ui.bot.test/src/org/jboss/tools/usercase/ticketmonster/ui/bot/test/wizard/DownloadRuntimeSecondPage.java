package org.jboss.tools.usercase.ticketmonster.ui.bot.test.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.RadioButton;

public class DownloadRuntimeSecondPage extends WizardPage{

	public void acceptLicense(boolean accept){
		if(accept){
			new RadioButton("I accept the terms of the license agreement").click();
		}else {
			new RadioButton("I do not accept the terms of the license agreement").click();
		}
	}
	
}
