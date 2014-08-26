package org.jboss.ide.eclipse.as.reddeer.server.wizard.page;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardPage;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * Adds error check to {@link NewServerWizardPage}
 * 
 * @author psrna
 * @author Radoslav Rabara
 * 
 */

public class NewServerWizardPageWithErrorCheck extends NewServerWizardPage {

	protected final static Logger log = Logger.getLogger(NewServerWizardPageWithErrorCheck.class);
	
	public String getServerName() {
		return new LabeledText("Server name:").getText();
	}

	public void checkErrors() {
		String errorText = getErrorText();
		if(errorText == null)
			return;
		checkServerName(errorText);
	}

	private String getErrorText() {
		String text;
		try {
			text = new LabeledText("Define a New Server").getText();
			log.info("Found error text: " + text);
		} catch(SWTLayerException e) {
			log.info("No error text found.");
			return null;
		}
		return text;
	}
	
	private void checkServerName(String errorText) {
		if(errorText.contains("The server name is already in use. Specify a different name.")) {
			throw new AssertionError("The server name '" + getServerName() + "' is already in use.");
		}
	}
}

