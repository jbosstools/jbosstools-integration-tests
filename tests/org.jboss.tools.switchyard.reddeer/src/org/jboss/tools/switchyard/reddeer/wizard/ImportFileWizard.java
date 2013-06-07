package org.jboss.tools.switchyard.reddeer.wizard;

import java.io.File;

import org.jboss.reddeer.eclipse.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.swt.util.Bot;

/**
 * 
 * @author apodhrad
 * 
 */
public class ImportFileWizard extends ImportWizardDialog {

	public ImportFileWizard() {
		super("General", "File System");
	}

	public void importFile(String folder, String fileName) {
		File file = new File(folder);
		if (!file.exists()) {
			throw new RuntimeException("File '" + folder + "' not found!");
		}

		open();

		Bot.get().comboBoxWithLabel("From directory:").setText(file.getAbsolutePath());
		Bot.get().tree().setFocus();
		Bot.get().table().getTableItem(fileName).check();

		finish();
	}

	public void importFolder(String path, String importFolder) {
		File file = new File(path);
		if (!file.exists()) {
			throw new RuntimeException("File '" + path + "' not found!");
		}

		open();

		Bot.get().comboBoxWithLabel("From directory:").typeText(file.getAbsolutePath());
		Bot.get().tree().setFocus();
		Bot.get().tree().getTreeItem(importFolder).check();

		finish();
	}
}
