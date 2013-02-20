package org.jboss.tools.teiid.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author apodhrad
 *
 */
public class ModelProjectPage extends WizardPage {

	public static final String LABEL_PROJECT_NAME = "Project name:";
	
	protected ModelProjectPage(WizardDialog wizardDialog) {
		super(wizardDialog, 1);
	}
	
	public void setProjectName(String projectName) {
		show();
		new LabeledText(LABEL_PROJECT_NAME).setText(projectName);
	}
}
