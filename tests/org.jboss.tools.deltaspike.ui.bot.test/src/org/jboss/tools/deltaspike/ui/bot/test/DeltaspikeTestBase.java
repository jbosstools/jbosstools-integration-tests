package org.jboss.tools.deltaspike.ui.bot.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEclipseEditor;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.Project;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tab.DefaultTabItem;
import org.jboss.reddeer.swt.impl.tree.ShellTreeItem;
import org.jboss.reddeer.swt.matcher.RegexMatchers;
import org.jboss.reddeer.swt.regex.Regex;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.deltaspike.ui.bot.test.condition.SpecificProblemExists;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.config.Annotations.Server;
import org.jboss.tools.ui.bot.ext.config.Annotations.ServerState;
import org.jboss.tools.ui.bot.ext.helper.ImportHelper;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@Require(clearProjects = true, server = @Server(state = ServerState.NotRunning, version = "6.0", operator = ">="))
@RunWith(RequirementAwareSuite.class)
@SuiteClasses({ DeltaspikeAllBotTests.class })
public class DeltaspikeTestBase extends SWTTestExt {

	private static final File DELTASPIKE_LIBRARY_DIR = new File(
			System.getProperty("deltaspike.libs.dir"));

	protected static final PackageExplorer packageExplorer = new PackageExplorer();
	protected static final ProblemsView problemsView = new ProblemsView();

	@AfterClass
	public static void cleanUp() {
		deleteAllProjects();
	}

	protected static void importDeltaspikeProject(String projectName) {
		/**
		 * TODO import project via reddeer (need to copy it to not modify test
		 * project)
		 **/
		ImportHelper.importProject("/resources/prj/" + projectName,
				projectName, Activator.PLUGIN_ID);
		addRuntimeIntoProject(configuredState.getServer().name, projectName);
		addDeltaspikeLibrariesIntoProject(projectName);
		cleanProjects();
	}

	protected void insertIntoFile(String projectName, String packageName,
			String bean, int line, int column, String insertedText) {
		openClass(projectName, packageName, bean);
		SWTBotEclipseEditor editor = bot.activeEditor().toTextEditor();
		editor.insertText(line, column, insertedText);
		editor.save();
	}

	protected void annotateBean(String projectName, String packageName,
			String bean, int line, int column, String annotation) {

		insertIntoFile(projectName, packageName, bean, line, column, annotation);
		new WaitUntil(new SpecificProblemExists(new Regex(
				".*cannot be resolved.*")), TimePeriod.NORMAL);

		SWTBotEclipseEditor editor = bot.activeEditor().toTextEditor();
		editor.setFocus();
		RegexMatchers m = new RegexMatchers("Source", "Organize Imports.*");
		new ShellMenu(m.getMatchers()).select();

		editor.save();

	}

	protected void openClass(String projectName, String packageName,
			String classFullName) {

		packageExplorer.open();
		packageExplorer.getProject(projectName)
				.getProjectItem("src", packageName, classFullName).open();

	}
	
	protected static void cleanProjects() {
		
		RegexMatchers m = new RegexMatchers("Project", "Clean.*");
		new ShellMenu(m.getMatchers()).select();
		new WaitUntil(new ShellWithTextIsActive("Clean"));
		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive("Clean"));
		new WaitWhile(new JobIsRunning());
		
	}

	private static void deleteAllProjects() {
		packageExplorer.open();
		for (Project project : packageExplorer.getProjects()) {
			project.delete(true);
		}
	}

	private static void addRuntimeIntoProject(String runtimeName,
			String projectName) {

		packageExplorer.open();
		packageExplorer.getProject(projectName).select();

		/* will be simpler in the future -> new TargetedRuntimesProperties() */
		new ContextMenu("Properties").select();
		Shell shell = new DefaultShell();
		new ShellTreeItem("Targeted Runtimes").select();
		new CheckBox("Show all runtimes").toggle(true);
		SWTBotTable table = Bot.get().table();
		for (int i = 0; i < table.rowCount(); i++) {
			table.getTableItem(i).uncheck();
		}
		table.getTableItem(runtimeName).check();

		new PushButton("OK").click();
		new WaitWhile(new ShellWithTextIsActive(shell.getText()),
				TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new WaitWhile(new ShellWithTextIsActive("Progress Information"),
				TimePeriod.LONG);

	}

	private static void addDeltaspikeLibrariesIntoProject(String projectName) {

		File[] libraries = addLibraryIntoProjectFolder(projectName,
				DELTASPIKE_LIBRARY_DIR);
		if (libraries == null)
			return;
		/** refresh the workspace **/
		new ShellMenu("File", "Refresh").select();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

		packageExplorer.open();
		packageExplorer.getProject(projectName).select();

		new ContextMenu("Properties").select();
		new DefaultShell();
		new ShellTreeItem("Java Build Path").select();
		new DefaultTabItem("Libraries").activate();

		for (File library : libraries) {
			new PushButton("Add JARs...").click();
			new WaitUntil(new ShellWithTextIsActive("JAR Selection"),
					TimePeriod.NORMAL);
			new DefaultShell("JAR Selection");
			new ShellTreeItem(projectName, library.getName()).select();
			new WaitUntil(new ButtonWithTextIsActive("OK"));
			new PushButton("OK").click();
			new WaitWhile(new ShellWithTextIsActive("JAR Selection"),
					TimePeriod.NORMAL);
		}
		new PushButton("OK").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

	}

	private static File[] addLibraryIntoProjectFolder(String projectName,
			File librariesFolder) {
		FileChannel inChannel = null;
		FileChannel outChannel = null;

		assertNotNull("You have to provide location of folder with "
				+ "library with property 'deltaspike.libs.dir'",
				DELTASPIKE_LIBRARY_DIR);
		List<File> libraryFiles = new ArrayList<File>();
		try {
			for (File in : librariesFolder.listFiles()) {
				if (in.isDirectory() || 
					!in.getName().substring(in.getName().lastIndexOf(".") + 1).equals("jar")) {
					continue;
				}
				File out = new File(Platform.getLocation() + File.separator
						+ projectName + File.separator + File.separator
						+ in.getName());

				inChannel = new FileInputStream(in).getChannel();
				outChannel = new FileOutputStream(out).getChannel();

				inChannel.transferTo(0, inChannel.size(), outChannel);
				libraryFiles.add(in);
			}
		} catch (IOException ioException) {

		}

		try {
			if (inChannel != null)
				inChannel.close();
			if (outChannel != null)
				outChannel.close();
		} catch (IOException ioException) {

		}
		return libraryFiles.toArray(new File[libraryFiles.size()]);
	}

}
