package org.jboss.tools.drools.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.CheckBox;

public class NewDslSamplesWizardPage extends WizardPage {

    public void setAddSampleDsl(boolean value) {
        new CheckBox("Create the DSL file with some sample DSL statements").toggle(value);
    }

}
