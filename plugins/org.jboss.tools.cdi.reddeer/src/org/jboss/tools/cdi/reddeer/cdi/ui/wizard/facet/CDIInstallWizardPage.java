package org.jboss.tools.cdi.reddeer.cdi.ui.wizard.facet;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.CheckBox;

public class CDIInstallWizardPage extends WizardPage{
	
	public void toggleCreateBeansXml(boolean checked){
		new CheckBox(0).toggle(checked);
	}
	
	public boolean isCreateBeansXml(){
		return new CheckBox(0).isChecked();
	}

}
