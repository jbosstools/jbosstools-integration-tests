package org.jboss.tools.cdi.reddeer.xhtml;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class NewXHTMLFileWizardPage extends WizardPage{
	
	private static final String PARENT_FOLDER_LABEL = "Enter or select the parent folder:";
	private static final String FILE_NAME_LABEL = "File name:";
	
	public void setDestination(String destination) {
		new LabeledText(PARENT_FOLDER_LABEL).setText(destination);
	}
	
	public void setName(String nameOfPage) {
		new LabeledText(FILE_NAME_LABEL).setText(nameOfPage);
	}

}
