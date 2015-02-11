package org.jboss.tools.hibernate.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Export Hibernate Code Generation Configuration to Ant Script wizard page
 * @author Jiri Peterka
 *
 */
public class ExportAntCodeGenWizardPage extends WizardPage {

	/**
	 * Sets given generation configuration
	 */
	public void setHibernateGenConfiguration(String genConfiguration) {
		new DefaultCombo().setSelection(genConfiguration);
	}

	/**
	 * Sets given generation configuration
	 */
	public void setAntFileName(String fileName) {
		new LabeledText("File name:").setText(fileName);
	}

}
