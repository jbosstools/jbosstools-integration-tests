package org.jboss.tools.openshift.ui.bot.test.app;

import java.io.File;
import java.io.IOException;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftUI;
import org.jboss.tools.openshift.ui.bot.util.TestProperties;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.Before;
import org.junit.Test;

public class ImportApp extends SWTTestExt {

	@Before
	public void cleanUpProject() {
		File gitDir = new File(System.getProperty("user.home") + "/git");

		boolean exists = gitDir.exists() ? true : gitDir.mkdir();

		if (exists && gitDir.isDirectory() && gitDir.listFiles().length > 0) {
			for (File file : gitDir.listFiles()) {
				if (file.getName().contains(
						TestProperties.get("openshift.jbossapp.name")))
					try {
						delete(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
	}

	@Test
	public void canImportAppFromExplorer() {
		SWTBotView openshiftExplorer = open
				.viewOpen(OpenShiftUI.Explorer.iView);

		SWTBotTreeItem account = openshiftExplorer.bot().tree().getAllItems()[0] // get
																					// 1st
																					// account
																					// in
																					// OpenShift
																					// Explorer
				.doubleClick(); // expand account

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);

		account.getNode(0).contextMenu(OpenShiftUI.Labels.EXPLORER_IMPORT_APP)
				.click();

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_20S, TIME_1S);
		bot.waitForShell(OpenShiftUI.Shell.IMPORT_APP);
		bot.button(IDELabel.Button.FINISH).click();
		
		bot.waitForShell("Question");
		bot.button(IDELabel.Button.YES).click();
		
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);

		// TODO workaround for: https://issues.jboss.org/browse/JBIDE-13560
		assertTrue(servers.serverExists(TestProperties
				.get("openshift.jbossapp.name") + " at Openshift"));

		log.info("*** OpenShift SWTBot Tests: OpenShift Server Adapter created. ***");
	}

	private void delete(File file) throws IOException {

		if (file.isDirectory()) {
			// directory is empty, then delete it
			if (file.list().length == 0) {
				file.delete();
				log.debug("Directory is deleted : " + file.getAbsolutePath());
			} else {
				// list all the directory contents
				String files[] = file.list();

				for (String temp : files) {
					// construct the file structure
					File fileDelete = new File(file, temp);
					// recursive delete
					delete(fileDelete);
				}

				// check the directory again, if empty then delete it
				if (file.list().length == 0) {
					file.delete();
					log.debug("Directory is deleted : "
							+ file.getAbsolutePath());
				}
			}
		} else {
			// if file, then delete it
			file.delete();
			log.debug("File is deleted : " + file.getAbsolutePath());
		}
	}

}
