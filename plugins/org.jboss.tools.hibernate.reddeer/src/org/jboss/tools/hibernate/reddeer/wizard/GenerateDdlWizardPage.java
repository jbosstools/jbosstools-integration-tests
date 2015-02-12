package org.jboss.tools.hibernate.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardPage;
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

}
