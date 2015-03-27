package org.jboss.tools.cdi.reddeer.cdi.ui.wizard.facet;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.LabeledCheckBox;

public class CDIInstallWizardPage extends WizardPage{
	
	public void toggleCreateBeansXml(boolean checked){
		new LabeledCheckBox("Generate beans.xml file:").toggle(checked);
	}
	
	public boolean isCreateBeansXml(){
		return new LabeledCheckBox("Generate beans.xml file:").isChecked();
	}

}
