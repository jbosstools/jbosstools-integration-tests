package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * SFTP binding page
 * 
 * @author apodhrad
 * 
 */
public class SFTPBindingPage extends OperationOptionsPage<SFTPBindingPage> {

	public static final String NAME = "Name";
	public static final String DIRECTORY = "Directory*";

	public SFTPBindingPage setName(String name) {
		new LabeledText(NAME).setFocus();
		new LabeledText(NAME).setText(name);
		return this;
	}

	public String getName() {
		return new LabeledText(NAME).getText();
	}

	public SFTPBindingPage setDirectory(String directory) {
		new LabeledText(DIRECTORY).setFocus();
		new LabeledText(DIRECTORY).setText(directory);
		new LabeledText(NAME).setFocus();
		return this;
	}

	public String getDirectory() {
		return new LabeledText(DIRECTORY).getText();
	}

}
