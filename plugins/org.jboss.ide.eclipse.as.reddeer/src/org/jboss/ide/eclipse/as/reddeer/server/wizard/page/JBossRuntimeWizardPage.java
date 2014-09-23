package org.jboss.ide.eclipse.as.reddeer.server.wizard.page;

import java.io.File;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Represents JBoss Runtime page displayed when adding JBoss Runtime via New Server dialog.
 * It's the next page displayed after invoking next page from New Server page,
 * only when any JBoss Runtime was selected on New Server page.
 * 
 * @author psrna
 * @author Radoslav Rabara
 * 
 */

public class JBossRuntimeWizardPage {
	
	private static final Logger LOGGER = Logger.getLogger(JBossRuntimeWizardPage.class);
	
	private static final String RUNTIME_NAME_LABEL = "Name";
	
	private static final String HOME_DIRECTORY_LABEL = "Home Directory";
	
	public void setRuntimeName(String name){
		 new LabeledText(RUNTIME_NAME_LABEL).setText(name);
	}

	public String getRuntimeName() {
		return new LabeledText(RUNTIME_NAME_LABEL).getText();
	}

	public void setRuntimeDir(String path){
		if(!new File(path).exists()) {
			throw new IllegalArgumentException("Path doesn't exist: "+path);
		}
		new LabeledText(HOME_DIRECTORY_LABEL).setText(path);
	}

	public String getRuntimeDir() {
		return new LabeledText(HOME_DIRECTORY_LABEL).getText();
	}

	public void checkErrors() {
		String text;
		try {
			text = new LabeledText("JBoss Runtime").getText();
			LOGGER.info("Found error text: " + text);
		} catch(SWTLayerException e) {
			LOGGER.info("No error text found.");
			return;
		}
		
		checkServerName(text);
		checkHomeDirectory(text);
		checkOtherErrors(text);
	}

	private void checkServerName(String errorText) {
		if(errorText.contains("Runtime name already in use")) {
			throw new AssertionError("The server name '"+getRuntimeName()+"' is already in use.");
		}
		if(errorText.contains("The name field must not be blank")) {
			throw new AssertionError("The server name is empty.");
		}
	}

	private void checkHomeDirectory(String errorText) {
		if(errorText.contains("The home directory does not exist or is not a directory.")) {
			throw new AssertionError("The home directory '"+getRuntimeDir()+"'"
					+" does not exist or is not a directory.");
		}
		if(errorText.contains("The home directory is missing a required file or folder:")) {
			throw new AssertionError("The home directory '"+getRuntimeDir()+"'"
					+" is missing a required file or folder:"+errorText.split(":")[1]);
		}
	}

	private void checkOtherErrors(String errorText) {
		if(errorText.contains("No valid JREs found for execution environment")) {
			throw new AssertionError(errorText);
		}
	}
}
