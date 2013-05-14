package org.jboss.tools.teiid.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author apodhrad
 * 
 */
public class ModelProjectPage extends WizardPage {

	public static final String LABEL_PROJECT_NAME = "Project name:";

	public void setProjectName(String projectName) {
		new LabeledText(LABEL_PROJECT_NAME).setText(projectName);
	}

	@Override
	public void fillWizardPage(Object... obj) {
		if (obj.length > 0 && obj[0] instanceof String) {
			setProjectName((String) obj[0]);
		} else {
			super.fillWizardPage(obj);
		}
	}
}
