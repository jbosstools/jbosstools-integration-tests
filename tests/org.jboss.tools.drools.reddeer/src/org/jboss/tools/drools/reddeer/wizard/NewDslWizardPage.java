package org.jboss.tools.drools.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class NewDslWizardPage extends WizardPage {

    public void setDslName(String fileName) {
        new LabeledText("File name:").setText(fileName);
    }

}
