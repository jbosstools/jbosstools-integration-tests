package org.jboss.tools.ui.bot.ext.helper;

import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellCloses;
import static org.eclipse.swtbot.swt.finder.waits.Conditions.widgetIsEnabled;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.Import.GeneralExistingProjectsintoWorkspace;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.wizards.SWTBotImportWizard;

/**
 * Heper class for project imports
 */
public class ImportHelper {
	
	private static SWTBotExt bot = new SWTBotExt();
	
	/**
	 * Import all projects from from given path to current workspace
	 */
	public static void importAllProjects(String path) {
		int timeout = 100000; // 100s max timeout
		bot.menu("File").menu("Import...").click();
		
		SWTBot dlgBot = bot.shell("Import").activate().bot();
		dlgBot.tree().expandNode("General").expandNode("Existing Projects into Workspace").select();
		dlgBot.button(IDELabel.Button.NEXT).click();
		
		dlgBot.radio(0).click();
		dlgBot.text().setText(path);
		dlgBot.radio(1).click();
		dlgBot.radio(0).click();
		dlgBot.button("Select All").click();
		SWTBotShell s = dlgBot.activeShell();
		bot.waitUntil(widgetIsEnabled(dlgBot.button(IDELabel.Button.FINISH)),timeout);
		dlgBot.button(IDELabel.Button.FINISH).click();
		bot.waitUntil(shellCloses(s),timeout);
	}	
	
	/**
	 * Import project from given location to destination directory
	 * @param projectLocation
	 * @param dir
	 * @param activatorPlugIn
	 */
	public static void importProject(String projectLocation, String dir, String activatorPlugIn) {
		String rpath = ResourceHelper.getResourceAbsolutePath(activatorPlugIn, projectLocation);
		String wpath = ResourceHelper.getWorkspaceAbsolutePath() + "/" + dir;
		File rfile = new File(rpath);
		File wfile = new File(wpath);
		
		wfile.mkdirs();
		try {
			FileHelper.copyFilesBinaryRecursively(rfile, wfile, null);
		} catch (IOException e) {
			fail("Unable to copy test project");
		}
		ImportHelper.importAllProjects(wpath);
		SWTUtilExt util = new SWTUtilExt(bot);
		util.waitForNonIgnoredJobs();	
	}
	
	/**
	 * Import project from a given zip location
	 * 
	 * @param projectLocation
	 */
	public static void importProjectFromZip(String projectLocation) {
		importProject(projectLocation, true);
	}
	
	/**
	 * Import project from a given location
	 * 
	 * @param projectLocation
	 */
	public static void importProject(String projectLocation) {
		importProject(projectLocation, false);
	}
	
	/**
	 * Import project from a given absolute location
	 * 
	 * @param projectLocation
	 * @param izZip 
	 */
	public static void importProject(String projectLocation, boolean isZip) {
		SWTBotImportWizard wizard = new SWTBotImportWizard();
		wizard.open(GeneralExistingProjectsintoWorkspace.LABEL);
		int index = 0;
		if(isZip) {
			index = 1;
		}
		wizard.bot().radio(index).click();
		wizard.bot().text(index).setText(projectLocation);
		wizard.bot().button(IDELabel.Button.REFRESH).click();
		wizard.finishWithWait();	
	}
}
