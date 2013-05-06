package org.jboss.tools.teiid.reddeer.wizard;

import java.io.File;

import org.jboss.reddeer.eclipse.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.swt.util.Bot;

public class ImportFileWizard extends ImportWizardDialog {

	public ImportFileWizard() {
		super("General", "File System");
	}

	public void importFile(String path, String importFolder) {
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
