package org.jboss.tools.teiid.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author apodhrad
 *
 */
public class ConnectionProfileXmlLocalPage extends WizardPage implements ConnectionProfileXmlPage {

	public static final String LABEL_FILE_NAME = "File Name";
	
	protected ConnectionProfileXmlLocalPage(WizardDialog wizardDialog, int pageIndex) {
		super(wizardDialog, pageIndex);
	}

	@Override
	public void setPath(String path) {
		new LabeledText(LABEL_FILE_NAME).setText(path);
	}
	
}
