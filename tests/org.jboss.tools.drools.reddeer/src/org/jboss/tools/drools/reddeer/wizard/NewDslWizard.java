package org.jboss.tools.drools.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;

public class NewDslWizard extends NewWizardDialog {

    public NewDslWizardPage getFirstPage() {
        selectPage(1);
        return new NewDslWizardPage();
    }

    public NewDslSamplesWizardPage getSamplesPage() {
        selectPage(2);
        return new NewDslSamplesWizardPage();
    }

    public void createDefaultDsl(String path, String name) {
        open();
        NewDslWizardPage page = getFirstPage();
        page.setParentFolder(path);
        page.setDslName(name);
        getSamplesPage().setAddSampleDsl(true);
        finish();
    }
}
