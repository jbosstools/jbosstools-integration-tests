package org.jboss.tools.hibernate.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Generate Tables from Entities Page
 * @author Jiri Peterka
 *
 */
public class GenerateDdlWizardPage extends WizardPage {

	/**
	 * Sets output directory for ddl
	 */
	public void setOutputDirectory(String dir) {
		new LabeledText("Output directory:").setText(dir);
	}

	/**
	 * Sets ddl file name
	 */
	public void setFileName(String fileName) {
		new LabeledText("File name").setText(fileName);
	}

	/**
	 * Sets whether to use console configuration or not for ddl generation 
	 * @param useConsole if set to true hibernate console configuration will be used
	 */
	public void setUseConsoleConfiguration(boolean useConsole) {
		CheckBox cbUseConsole = new CheckBox("Use Console Configuration");
		if (cbUseConsole.isEnabled() != useConsole) {
			cbUseConsole.click();
		}
	}
	
	/**
	 * Set Hibernate Version for table/ddl generation
	 * @param hbVersion hibernate version 
	 */
	public void setHibernateVersion(String hbVersion) {
		LabeledCombo lc = new LabeledCombo("Hibernate Version:");
		lc.setSelection(hbVersion);
	}
}
