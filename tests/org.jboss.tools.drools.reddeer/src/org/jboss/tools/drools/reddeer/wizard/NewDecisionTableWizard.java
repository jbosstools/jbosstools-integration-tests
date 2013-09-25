package org.jboss.tools.drools.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;

public class NewDecisionTableWizard extends NewWizardDialog {

    public NewDecisionTableWizard() {
        super("Drools", "Decision Table");
    }

    public NewDecisionTableWizardPage getFirstPage() {
        return new NewDecisionTableWizardPage();
    }
}
