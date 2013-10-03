package org.jboss.tools.jbpm.ui.bot.test.wizard;

import java.io.File;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.eclipse.jface.wizard.ImportWizardDialog;

/**
 * 
 * @author apodhrad
 *
 */
public class ImportFileWizard extends ImportWizardDialog {

	protected static SWTWorkbenchBot bot = new SWTWorkbenchBot();
	
	public ImportFileWizard() {
		super("General", "File System");
	}

	public void importFile(String path, String importFolder) {
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
