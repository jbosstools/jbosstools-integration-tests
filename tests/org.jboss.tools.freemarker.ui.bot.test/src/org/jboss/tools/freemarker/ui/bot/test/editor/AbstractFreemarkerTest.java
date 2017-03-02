/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.freemarker.ui.bot.test.editor;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.common.platform.RunningPlatform;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.core.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.core.matcher.WithTextMatcher;
import org.jboss.reddeer.eclipse.condition.ConsoleHasText;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.eclipse.ui.views.log.LogMessage;
import org.jboss.reddeer.eclipse.ui.views.log.LogView;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;
import org.jboss.tools.freemarker.ui.bot.test.Activator;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Freemarker test parent
 * @author Jiri Peterka
 *
 */
public abstract class AbstractFreemarkerTest {
		
	private static final Logger log = Logger.getLogger(FreeMarkerBaseEditorTest.class);
	
	protected static String projectName = "org.jboss.tools.freemarker.testprj";
	
	protected static String parentFolder = "ftl";
	
	@BeforeClass
	public static void beforeClass() {
		setFullOutlineView();
	}
	
	@Before
	public void setUp() {
		log.step("Import test project for freemarker test");
		importTestProject();
	}
	
	@After
	public void after() {
		removeTestProject(projectName);
		ConsoleView console = new ConsoleView();
		if (console.isOpened()) {
			console.clearConsole();
			console.close();
		}
	}
	
	/**
	 * Sets Freemarker to full outline view mode
	 */
	public static void setFullOutlineView() {
		
		JavaPerspective p = new JavaPerspective();
		p.open();
		EditorHandler.getInstance().closeAll(false);
		new WaitWhile(new JobIsRunning());		
	
		JavaPerspective jp = new JavaPerspective();
		jp.open();
		
		WorkbenchPreferenceDialog dlg = new WorkbenchPreferenceDialog();
		dlg.open();
		dlg.select("FreeMarker");
		
		log.step("Set Freemarker outline level to full level on freemarker preference page");
		FreemarkerPreferencePage page = new FreemarkerPreferencePage();
		page.setOutlineLevelOfDetail(OutlineLevelOfDetail.FULL);
		
		dlg.ok();
	}
	
	/**
	 * Imports freemarker test project
	 */
	public static void importTestProject() {
		
		ExternalProjectImportWizardDialog wizard = new ExternalProjectImportWizardDialog();		
		wizard.open();

		String rpath = getResourceAbsolutePath(
				Activator.PLUGIN_ID, "resources/prj");
		String wpath = getWorkspaceAbsolutePath();
		File rfile = new File(rpath);
		File wfile = new File(wpath);
		try {
			copyFilesBinaryRecursively(rfile, wfile, null);
		} catch (IOException e) {
			fail("Unable to copy freemarker test project");
		}
		
		WizardProjectsImportPage firstPage = new WizardProjectsImportPage();
		
		firstPage.setRootDirectory(wpath);
		firstPage.selectAllProjects();
		firstPage.copyProjectsIntoWorkspace(false);
		wizard.finish();		
	}

	/**
	 * Check error log for errors
	 */
	public void checkErrorLog() {
		LogView el = new LogView();
		el.open();
		List<LogMessage> errorMessages = el.getErrorMessages();
		for (LogMessage lm : errorMessages) { 
			log.info(lm.getMessage());
		}
	}

	/**
	 * Read test file to string
	 * @param filePath file path
	 * @return content of the file
	 * @throws IOException
	 */
	public String readTextFileToString(String filePath) throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);				
				line = br.readLine();
				if (line != null)
					sb.append("\n");
			}
			String everything = sb.toString();
			return everything;
		} finally {
			br.close();
		}
	}
	
	/**
	 * Provide bundle resource absolute path
	 * @param pluginId - plugin id
	 * @param path - resource relative path
	 * @return resource absolute path
	 */
	public static String getResourceAbsolutePath(String pluginId, String... path) {

		// Construct path
		StringBuilder builder = new StringBuilder();
		for (String fragment : path) {
			builder.append("/" + fragment);
		}

		String filePath = "";
		try {
			filePath = FileLocator.toFileURL(
					Platform.getBundle(pluginId).getEntry("/")).getFile()
					+ "resources" + builder.toString();
			File file = new File(filePath);
			if (!file.isFile()) {
				filePath = FileLocator.toFileURL(
						Platform.getBundle(pluginId).getEntry("/")).getFile()
						+ builder.toString();
			}
		} catch (IOException ex) {
			String message = filePath + " resource file not found";
			//log.error(message);
			fail(message);
		}

		return filePath;
	}
	
	/**
	 * Gets workspace absolute path 
	 * @return current workspace absolute path
	 */
	public static String getWorkspaceAbsolutePath() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		String path = workspace.getRoot().getLocation().toFile().getAbsolutePath();  
		return path;		
	}	
	
	/**
	 * Recursively copies files and subdirectories from fromLocation to
	 * toLocation using FileFilter fileFliter
	 * 
	 * @param fromLocation
	 * @param toLocation
	 * @param fileFilter
	 * @throws IOException
	 */
	public static void copyFilesBinaryRecursively(File fromLocation,
			File toLocation, FileFilter fileFilter) throws IOException {
		if (fromLocation.exists()) {
			for (File fileToCopy : fromLocation.listFiles(fileFilter)) {
				if (fileToCopy.isDirectory()) {
					File newToDir = new File(toLocation, fileToCopy.getName());
					newToDir.mkdir();
					copyFilesBinaryRecursively(fileToCopy, newToDir, fileFilter);
				} else {
					copyFilesBinary(fileToCopy, toLocation);
				}
			}
		}
	}
	
	/**
	 * Remove freemarker test project
	 */
	public static void removeTestProject(String prj) {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		if (pe.containsProject(prj)) {
			pe.getProject(prj).delete(true);
		}
	}
	
	/**
	 * Copies binary file originalFile to location toLocation
	 * 
	 * @param originalFile
	 * @param toLocation
	 * @throws IOException
	 */
	public static void copyFilesBinary(File originalFile, File toLocation)
			throws IOException {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(originalFile);
			fos = new FileOutputStream(new File(toLocation,
					originalFile.getName()));
			byte[] buffer = new byte[4096];
			int bytesRead;

			while ((bytesRead = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, bytesRead); // write
			}

		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// do nothing
				}
			}
			if (fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					// do nothing
				}
			}
		}
	}
	
	/**
	 * Emtpy Error Log View
	 */
	public void emptyErrorLog() {
		
		LogView elv = new LogView();
		elv.open();
		elv.deleteLog();
		new WaitWhile(new JobIsRunning());
	}
	
	/**
	 * Checks whether executed freemarker template is resulting correct output
	 * @param outputIsInFile boolean value that distinguishes if expected output is in String or will
	 * be read from file
	 * 
	 * @param output expected output source (file or String)
	 * @param alternateOutput alternate output string
	 * @param runFile java file to be run as Java Application
	 */
	protected void checkFreemMarkerOutput(boolean outputIsInFile, String output, 
			String alternateOutput, String... runFile) {
		
		String outputExpected = "";
		if (outputIsInFile) {
			String outputFilepath = getResourceAbsolutePath(
					Activator.PLUGIN_ID, output);
		
			outputExpected = readOutputFromFile(outputFilepath);
		} else {
			outputExpected = output;
		}

		runJavaFile(runFile);
		
		new WaitUntil(new ConsoleHasText(outputExpected), TimePeriod.NORMAL, false);
		ConsoleView cv = new ConsoleView();
		cv.open();
		String consoleText = cv.getConsoleText();
		
		if (!consoleText.equals(outputExpected)) {

		}
		// workaround for slightly different format on Windows
		compareConsoleToOutput(consoleText, outputExpected, alternateOutput);
	}
	
	/**
	 * If given alternate expected result, checks whether we are running on Windows platform,
	 * if yes, it checks whether console output contains alternate output. Otherwise it tries if 
	 * expected output and resulting console output are equal, using String equals method.
	 * Otherwise it checks whether trimmed console output is equal to trimmed expected output.
	 * 
	 * @param console String content of console that will be compared
	 * @param output Expected output represented by String
	 * @param alternateOutput Alternate output used for Windows platform
	 */
	private void compareConsoleToOutput(String console, String output, String alternateOutput) {
		if (RunningPlatform.isWindows() && alternateOutput != null && alternateOutput.length() > 0) {
			log.info("Testing alternative output for windows platform: \n" + alternateOutput);
			output = alternateOutput.replaceAll("(\\r|\\n|\\r\\n)+", System.lineSeparator());
			console = console.replaceAll("(\\r|\\n|\\r\\n)+", System.lineSeparator());
		}
		checkOutput(console, output);
	}
	
	/**
	 * Compares two test outputs and reports error if text objects does not match
	 * after calling equals, equals (with trimmed text) and contains with trimmed methods
	 * @param consoleText text obtained from console
	 * @param expectedText 
	 */
	private void checkOutput(String consoleText, String expectedText) {
		if (consoleText.equals(expectedText)) {
			log.info("Console text corresponds to expected output");
		} else if (consoleText.trim().equals(expectedText.trim())) {
			log.info("Console text corresponds to expected output after trimming");
		} else if (consoleText.trim().contains(expectedText.trim())) {
			log.info("Console text contains expected output after trimming");
		} else {
			log.error("Console text doesn't correspond with expected text");
			log.dump("Console text:\n" + consoleText);
			log.dump("Expected text:\n" + expectedText);
			fail("Console text does not corresponds to the expected output");
		}		
	}
	
	/**
	 * Returns content of a file as a String
	 * 
	 * @param absolutePath absolute file path
	 * @return Content of given file specified by absolute path as a String
	 */
	private String readOutputFromFile(String absolutePath) {
		String outputExpected = "";
		try {
			outputExpected = readTextFileToString(absolutePath);
			log.info("------------------------------------------");
			log.info(outputExpected);
			log.info("------------------------------------------");
		} catch (IOException e) {
			log.error(e.getMessage());
			new RuntimeException("Unable to read from resource");
		}
		return outputExpected;
	}
	
	/**
	 * Tries to run given file path in Project explorer as a Java Application
	 * 
	 * @param javaFilePath File path specified as a array of strings
	 */
	@SuppressWarnings("unchecked")
	private void runJavaFile(String... javaFilePath) {
		PackageExplorer explorer = new PackageExplorer();
		Project project = explorer.getProject(projectName);
		ProjectItem item = project.getProjectItem(javaFilePath);
		item.select();
		WithTextMatcher regex = new WithTextMatcher(new RegexMatcher(".*Java Application.*"));
		new ContextMenu(new WithTextMatcher("Run As"), regex).select();

		new WaitUntil(new ShellWithTextIsAvailable("Progress Information"), TimePeriod.NORMAL, false);
		new WaitWhile(new ShellWithTextIsAvailable("Progress Information"));
		new WaitWhile(new JobIsRunning());		
	}


}
