package org.jboss.tools.jbpm.ui.bot.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.tools.jbpm.ui.bot.test.Activator;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.entity.JavaProjectEntity;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.Test;

@Require(clearProjects = true, perspective = "Java")
public class BPMNConvertCase extends SWTTestExt {

	private String projectName = "BPMNConvertProject";
	private String originalFolder = "original";
	private String targetFolder = "target";
	private String file1 = "PolicyPricingProcess.bpmn";
	private String file2 = "PolicyPricingProcess.bpmn_diagram";

	@Test
	public void createProject() {
		// Create Java Project
		JavaProjectEntity project = new JavaProjectEntity();
		project.setProjectName(projectName);
		eclipse.createJavaProject(project);

		// create original folder
		packageExplorer.selectProject(projectName);
		bot.menu("File").menu("New").menu("Folder").click();
		bot.textWithLabel("Folder name:").setText(originalFolder);
		open.finish(bot);

		// create target folder
		packageExplorer.selectProject(projectName);
		bot.menu("File").menu("New").menu("Folder").click();
		bot.textWithLabel("Folder name:").setText(targetFolder);
		open.finish(bot);
	}

	@Test
	public void importFiles() {
		// file1
		File in = SWTUtilExt.getResourceFile(Activator.PLUGIN_ID, "original",
				file1);
		File out = new File(Platform.getLocation() + File.separator
				+ projectName + File.separator + originalFolder
				+ File.separator + file1);
		copyFile(in, out);

		// file2
		in = SWTUtilExt.getResourceFile(Activator.PLUGIN_ID, "original", file2);
		out = new File(Platform.getLocation() + File.separator + projectName
				+ File.separator + originalFolder + File.separator + file2);
		copyFile(in, out);

		// refresh
		packageExplorer.selectProject(projectName);
		bot.menu("File").menu("Refresh").click();

		util.waitForNonIgnoredJobs();

		eclipse.openFile(projectName, originalFolder, file1);
		eclipse.openFile(projectName, originalFolder, file2);

		bot.closeAllEditors();
	}

	@Test
	public void convertProcess() {
		SWTBot viewBot = bot.viewByTitle(IDELabel.View.PACKAGE_EXPLORER).bot();		
		SWTEclipseExt.selectTreeLocation(viewBot,projectName, originalFolder, file1);
		
		open.newExport(ActionItem.Export.BPMNBPMNtojPDL.LABEL);
		bot.clickButton(IDELabel.Button.NEXT);
		bot.clickButton(IDELabel.Button.NEXT);
		bot.clickButton(IDELabel.Button.NEXT);

		bot.tree().expandNode(projectName).select(targetFolder);
		open.finish(bot);

		// refresh target folder
		packageExplorer.selectProject(projectName);
		bot.menu("File").menu("Refresh").click();

		// open converted jbpm file
		eclipse.openFile(projectName, targetFolder, "jpdl", file1,
				"Policy Pricing", "processdefinition.xml");

		bot.sleep(TIME_10S);
	}

	private void copyFile(File in, File out) {
		try {

			FileChannel inChannel = null;
			FileChannel outChannel = null;

			inChannel = new FileInputStream(in).getChannel();
			outChannel = new FileOutputStream(out).getChannel();

			inChannel.transferTo(0, inChannel.size(), outChannel);

			if (inChannel != null)
				inChannel.close();
			if (outChannel != null)
				outChannel.close();
			log.info("File " + in.getAbsolutePath() + " copied");
		} catch (Exception e) {
			log.error(e.getMessage());
			fail("Error during copying files " + in.getAbsolutePath() + " - "
					+ e.getMessage());
		}
	}

}
