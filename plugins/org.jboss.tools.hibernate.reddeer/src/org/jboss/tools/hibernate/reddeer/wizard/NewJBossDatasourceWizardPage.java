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

	/**
	 * Sets connection profile for jboss ds wizard
	 * @param profileName given db profile
	 */
	public void setConnectionProfile(String profileName) {
		new LabeledCombo("Connection profile:").setSelection(profileName);
	}
	
	/**
	 * Sets parent folder for jboss ds wizard
	 * @param folder given folder name
	 */
	public void setParentFolder(String folder) {
		new LabeledText("Parent folder:").setText(folder);
	}
	
	/**
	 * Click finish
	 */
	public void finish() {
		new FinishButton().click(); 
	}
}
