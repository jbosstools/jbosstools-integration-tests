package org.jboss.tools.central.reddeer.wizards;

import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.tools.maven.reddeer.project.examples.wizard.MavenExamplesRequirementPage;

public class NewProjectExamplesWizardDialogCentral extends WizardDialog {

	public NewProjectExamplesWizardDialogCentral() {
		addWizardPage(new MavenExamplesRequirementPage(), 0);
		addWizardPage(new NewProjectExamplesLocationPage(), 1);
	}
	
}
