package org.jboss.tools.maven.ui.bot.test.dialog.seam;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;

public class SeamProjectFifthPage extends WizardPage{
	
	public void setSeamRuntime(String runtime){
		new DefaultCombo("Seam Runtime:").setSelection(runtime);
	}
	
	public void toggleEAR(boolean toggle){
		if(toggle){
			new RadioButton("EAR").click();
		} else {
			new RadioButton("WAR").click();
		}
	}

}
