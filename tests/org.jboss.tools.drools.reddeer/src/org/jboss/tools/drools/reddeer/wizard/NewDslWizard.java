package org.jboss.tools.drools.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;

public class NewDslWizard extends WizardDialog {

    public WizardPage getFirstPage() {
        selectPage(1);
        return new NewDslWizardPage();
    }

    public NewDslSamplesWizardPage getSamplesPage() {
        selectPage(2);
        return new NewDslSamplesWizardPage();
    }

}
