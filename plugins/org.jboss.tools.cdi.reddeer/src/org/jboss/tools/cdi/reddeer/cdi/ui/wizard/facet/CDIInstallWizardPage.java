package org.jboss.tools.cdi.reddeer.cdi.ui.wizard.facet;

import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.jface.wizard.WizardPage;
import org.eclipse.reddeer.swt.impl.button.LabeledCheckBox;

public class CDIInstallWizardPage extends WizardPage{
	
	public CDIInstallWizardPage(ReferencedComposite referencedComposite) {
		super(referencedComposite);
	}

	public void toggleCreateBeansXml(boolean checked){
		new LabeledCheckBox("Generate beans.xml file:").toggle(checked);
	}
	
	public boolean isCreateBeansXml(){
		return new LabeledCheckBox("Generate beans.xml file:").isChecked();
	}

}
