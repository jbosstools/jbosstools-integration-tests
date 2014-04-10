package org.jboss.tools.seam.reddeer.wizards;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;

public class SeamProjectFifthPage extends WizardPage{
	
	public void setSeamRuntime(String runtime){
		new LabeledCombo("Seam Runtime:").setSelection(runtime);
	}
	
	public void toggleEAR(boolean toggle){
		if(toggle){
			new RadioButton("EAR").click();
		} else {
			new RadioButton("WAR").click();
		}
	}

}
