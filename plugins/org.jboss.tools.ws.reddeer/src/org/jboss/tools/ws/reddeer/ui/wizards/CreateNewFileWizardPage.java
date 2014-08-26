package org.jboss.tools.ws.reddeer.ui.wizards;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Represents shared wizard page that is being used in wizards that creates a new
 * file in the specified project.
 *
 * @author Radoslav Rabara
 *
 */
public class CreateNewFileWizardPage extends WizardPage {

	/**
	 * Sets the file name.
	 *
	 * @param fileName name of the new file
	 */
	public void setFileName(String fileName) {
		new LabeledText("File name:").setText(fileName);
	}

	/**
	 * Sets the parent folder.
	 *
	 * @param parentFolder folder in which the file will be created
	 */
	public void setParentFolder(String parentFolder) {
		new LabeledText("Enter or select the parent folder:").setText(parentFolder);
	}
}
