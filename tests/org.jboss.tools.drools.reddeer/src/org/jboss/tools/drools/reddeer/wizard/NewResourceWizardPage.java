package org.jboss.tools.drools.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public abstract class NewResourceWizardPage extends WizardPage {

    public void setParentFolder(String parent) {
        new LabeledText("Enter or select the parent folder:").setText(parent);
    }

    public void setFileName(String fileName) {
        new LabeledText("File name:").setText(fileName);
    }

}
