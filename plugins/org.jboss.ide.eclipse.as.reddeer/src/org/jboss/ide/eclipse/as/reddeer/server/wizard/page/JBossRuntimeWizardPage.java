package org.jboss.ide.eclipse.as.reddeer.server.wizard.page;

import java.io.File;

import org.jboss.reddeer.junit.logging.Logger;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author psrna
 *
 */
public class JBossRuntimeWizardPage {
	
	private static final Logger LOGGER = Logger.getLogger(JBossRuntimeWizardPage.class);
	
	private final String runtimeNameLabel = "Name";
	
	private final String homeDirectoryLabel = "Home Directory";
	
	public void setRuntimeName(String name){
		 new LabeledText(runtimeNameLabel).setText(name);
	}

	public String getRuntimeName() {
		return new LabeledText(runtimeNameLabel).getText();
	}

	public void setRuntimeDir(String path){
		if(!new File(path).exists()) {
			throw new IllegalArgumentException("Path doesn't exist: "+path);
		}
		new LabeledText(homeDirectoryLabel).setText(path);
	}

	public String getRuntimeDir() {
		return new LabeledText(homeDirectoryLabel).getText();
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
