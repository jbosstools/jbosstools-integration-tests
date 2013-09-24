package org.jboss.tools.drools.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class NewDroolsProjectNameWizardPage extends WizardPage {
    public static final String LABEL_PROJECT_NAME = "Project name:";

    protected NewDroolsProjectNameWizardPage() {
    }

    public void setProjectName(String projectName) {
        new LabeledText(LABEL_PROJECT_NAME).setText(projectName);
    }
}
