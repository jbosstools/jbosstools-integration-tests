package org.jboss.tools.seam.reddeer.wizards;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;

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
	
	public void setConnectionProfile(String profile){
		new LabeledCombo(new DefaultGroup("Database"), "Connection profile:").setSelection(profile);
	}
	
	public void setDatabaseType(String type){
		new DefaultCombo(new DefaultGroup("Database"), "Database type:").setSelection(type);
	}
	

}
