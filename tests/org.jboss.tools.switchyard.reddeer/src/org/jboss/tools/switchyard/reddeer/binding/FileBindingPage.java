package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * File binding page
 * 
 * @author apodhrad
 * 
 */
public class FileBindingPage extends OperationOptionsPage<FileBindingPage> {

	public static final String DIR_AUTO_CREATION = "Auto Create Missing Directories in File Path";
	public static final String DIRECTORY = "Directory*";
	public static final String NAME = "Name";

	public FileBindingPage setName(String name) {
		new LabeledText(NAME).setFocus();
		new LabeledText(NAME).setText(name);
		return this;
	}

	public String getName() {
		return new LabeledText(NAME).getText();
	}

	public FileBindingPage setDirectory(String directory) {
		new LabeledText(DIRECTORY).setFocus();
		new LabeledText(DIRECTORY).setText(directory);
		new LabeledText(NAME).setFocus();
		return this;
	}

	public String getDirectory() {
		return new LabeledText(DIRECTORY).getText();
	}

	public FileBindingPage setDirAutoCreation(boolean dirAutoCreation) {
		new CheckBox(DIR_AUTO_CREATION).toggle(dirAutoCreation);
		return this;
	}

	public boolean isDirAutoCreation() {
		return new CheckBox(DIR_AUTO_CREATION).isChecked();
	}

	public FileBindingPage setMoveDirectory(String moveDirectory) {
		new LabeledText("Move (Default .camel)").setText(moveDirectory);
		return this;
	}

}
