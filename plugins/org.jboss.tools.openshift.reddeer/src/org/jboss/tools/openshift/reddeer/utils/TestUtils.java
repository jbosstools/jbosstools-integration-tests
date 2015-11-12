package org.jboss.tools.openshift.reddeer.utils;

import java.io.File;
import java.io.IOException;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;

public class TestUtils {

	public static void setVisualEditorToUseHTML5() {
		WorkbenchPreferenceDialog dialog = new WorkbenchPreferenceDialog();
		dialog.open();
		
		dialog.select("JBoss Tools", "Web", "Editors", "Visual Page Editor");
		
		RadioButton button = new RadioButton("HTML5 (use WebKit)");
		if (button.isEnabled() && !button.isSelected()) {
			button.click();
		}
		
		CheckBox checkBox = new CheckBox("Do not show Browser Engine dialog");
		if (checkBox.isEnabled() && !checkBox.isChecked()) {
			checkBox.click();
		}
		
		new PushButton("Apply").click();
		dialog.ok();
	}
	
	public static void cleanupGitFolder(String appname) {
		File gitDir = new File(System.getProperty("user.home") + "/git");

		boolean exists = gitDir.exists() ? true : gitDir.mkdir();

		if (exists && gitDir.isDirectory() && gitDir.listFiles().length > 0) {
			for (File file : gitDir.listFiles()) {
				if (file.getName().contains(appname))
					try {
						TestUtils.delete(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
	}

	public static void delete(File file) throws IOException {
		if (file.isDirectory() && file.list().length > 0) {
			String files[] = file.list();
			for (String tmpFile : files) {
				File fileToDelete = new File(file, tmpFile);
				delete(fileToDelete);
			}
		}
		
		file.delete();
	}

}
