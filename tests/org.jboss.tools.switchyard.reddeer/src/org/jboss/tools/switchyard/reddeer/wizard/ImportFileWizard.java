package org.jboss.tools.switchyard.reddeer.wizard;

import java.io.File;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.eclipse.jface.wizard.ImportWizardDialog;

/**
 * 
 * @author apodhrad
 * 
 */
public class ImportFileWizard extends ImportWizardDialog {

	private static SWTWorkbenchBot bot = new SWTWorkbenchBot(); 
	
	public ImportFileWizard() {
		super("General", "File System");
	}

	public void importFile(String folder, String fileName) {
		File file = new File(folder);
		if (!file.exists()) {
			throw new RuntimeException("File '" + folder + "' not found!");
		}

		open();

		bot.comboBoxWithLabel("From directory:").setText(file.getAbsolutePath());
		bot.tree().setFocus();
		bot.table().getTableItem(fileName).check();

		finish();
	}

	public void importFolder(String path, String importFolder) {
		File file = new File(path);
		if (!file.exists()) {
			throw new RuntimeException("File '" + path + "' not found!");
		}

		open();

		bot.comboBoxWithLabel("From directory:").typeText(file.getAbsolutePath());
		bot.tree().setFocus();
		bot.tree().getTreeItem(importFolder).check();

		finish();
	}
}
