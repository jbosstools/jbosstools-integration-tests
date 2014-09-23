package org.jboss.tools.hibernate.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.FinishButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Datasource wizard page
 * @author Jiri Peterka
 *
 */
public class NewJBossDatasourceWizardPage extends WizardPage {

	public void setConnectionProfile(String profileName) {
		new LabeledCombo("Connection profile:").setSelection(profileName);
	}
	
	public void setParentFolder(String folder) {
		new LabeledText("Parent folder:").setText(folder);
	}
	
	public void finish() {
		new FinishButton().click(); 
	}
}
