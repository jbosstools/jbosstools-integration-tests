package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * HTTP binding.
 * 
 * @author apodhrad
 * 
 */
public class FileBindingWizard extends BindingWizard<FileBindingWizard> {

	public static final String DIALOG_TITLE = "File Binding";

	public FileBindingWizard() {
		super("");
	}

	public FileBindingWizard setDirectory(String directory) {
		new LabeledText("Directory*").setText(directory);
		return this;
	}

	public FileBindingWizard setDirAutoCreation(boolean dirAutoCreation) {
		new CheckBox("Auto Create Missing Directories in File Path").toggle(dirAutoCreation);
		return this;
	}

	public FileBindingWizard setMoveDirectory(String moveDirectory) {
		new LabeledText("Move (Default .camel)").setText(moveDirectory);
		return this;
	}

}
