package org.jboss.tools.teiid.reddeer.wizard;

import java.io.File;

import org.jboss.reddeer.eclipse.jface.wizard.ImportWizardDialog;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;

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

		new SWTWorkbenchBot().comboBoxWithLabel("From directory:").typeText(file.getAbsolutePath());//problematic
		new SWTWorkbenchBot().tree().setFocus();
		new SWTWorkbenchBot().tree().getTreeItem(importFolder).check();

		finish();
	}
}
