package org.jboss.tools.central.reddeer.wizards;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.tools.central.reddeer.exception.CentralException;

public class NewProjectExamplesLocationPage extends WizardPage{
	
	public void toggleDefaultLocation(boolean checked){
		new CheckBox(0).toggle(checked);
	}
	
	public boolean isDefaultLocationChecked(){
		return new CheckBox(0).isChecked();
	}
	
	public String getLocation(){
		return new DefaultText(0).getText();
	}
	
	public void setLocation(String path){
		Text text = new DefaultText(0);
		if (text.isEnabled()){
			text.setText(path);
		}else{
			throw new CentralException("Location is not editable");
		}
	}

}
