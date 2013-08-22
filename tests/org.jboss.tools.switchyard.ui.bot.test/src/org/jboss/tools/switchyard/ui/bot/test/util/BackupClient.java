package org.jboss.tools.switchyard.ui.bot.test.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jface.wizard.ExportWizardDialog;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.tools.switchyard.ui.bot.test.suite.SwitchyardSuite;

public class BackupClient {

	public static void backupDeployment(String project) {
		String serverHome = SwitchyardSuite.getServerHome();
		copyfile(serverHome + "/standalone/deployments/" + project + ".jar", serverHome + "/../"
				+ project + ".jar");
	}

	public static void backupProject(String project) {
		String serverHome = SwitchyardSuite.getServerHome();
		new ExportProjectWizard().export(project, serverHome + "/../" + project + ".zip");
	}

	public static void copyfile(String srFile, String dtFile) {
		try {
			File f1 = new File(srFile);
			File f2 = new File(dtFile);

			InputStream in = new FileInputStream(f1);
			OutputStream out = new FileOutputStream(f2);

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			System.out.println("File copied.");
		} catch (FileNotFoundException ex) {
			System.out.println(ex.getMessage() + " in the specified directory.");
			System.exit(0);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public static class ExportProjectWizard extends ExportWizardDialog {

		public ExportProjectWizard() {
			super("General", "Archive File");
		}

		public void export(String project, String path) {
			new ProjectExplorer().getProject(project).select();

			open();

			new DefaultCombo("To archive file:").setText(path);

			finish();
		}
	}
}
