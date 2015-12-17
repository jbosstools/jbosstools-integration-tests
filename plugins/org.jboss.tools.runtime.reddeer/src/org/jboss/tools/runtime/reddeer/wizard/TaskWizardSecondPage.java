package org.jboss.tools.runtime.reddeer.wizard;

import org.jboss.reddeer.common.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.core.exception.CoreLayerException;
import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.RadioButton;

public class TaskWizardSecondPage extends WizardPage{

	public void acceptLicense(boolean accept){
		if(accept){
			
			try {
				new RadioButton("I accept the terms of the license agreement").click();
			} catch (CoreLayerException e) {
				// This can sometimes fail. Try again
				new RadioButton("I accept the terms of the license agreement").click();
			}
		}else {
			new RadioButton("I do not accept the terms of the license agreement").click();
		}
	}
	
}
