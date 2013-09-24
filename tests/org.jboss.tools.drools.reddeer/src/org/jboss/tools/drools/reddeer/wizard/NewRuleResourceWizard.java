package org.jboss.tools.drools.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;

public class NewRuleResourceWizard extends NewWizardDialog {

    public NewRuleResourceWizard() {
        super("Drools", "Rule Resource");
    }

    @Override
    public NewRuleResourceWizardPage getFirstPage() {
        return new NewRuleResourceWizardPage();
    }

}
