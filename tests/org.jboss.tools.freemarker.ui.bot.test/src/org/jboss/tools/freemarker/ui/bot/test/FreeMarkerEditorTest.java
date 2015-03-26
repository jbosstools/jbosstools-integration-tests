package org.jboss.tools.freemarker.ui.bot.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.reddeer.eclipse.ui.console.ConsoleView;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.eclipse.ui.views.contentoutline.OutlineView;
import org.jboss.reddeer.eclipse.ui.views.log.LogMessage;
import org.jboss.reddeer.eclipse.ui.views.log.LogView;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.handler.WorkbenchHandler;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Freemarker ui bot test
 * @author Jiri Peterka
 *
 */
@RunWith(RedDeerSuite.class)
public class FreeMarkerEditorTest {
	
	
	private static Logger log = Logger.getLogger(FreeMarkerEditorTest.class);
	private String prj = "org.jboss.tools.freemarker.testprj";
	
	@BeforeClass
	public static void beforeClass() {
		JavaPerspective p = new JavaPerspective();
		p.open();
		WorkbenchHandler.getInstance().closeAllEditors();
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

	@Test
	public void emptyTest() {
		assertTrue(true);
	}

	@Test
	public void freeMarkerTest() {
		emptyErrorLog();
		log.step("Import test project for freemarker test");
		importTestProject();
		log.step("Open ftl file in freemarker editor");
		openFTLFileInEditor();
		// disabled until target platform in running instance is resolved
		// checkFreemMarkerOutput();
		checkErrorLog();
	}

	private void importTestProject() {
		
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
		
		WizardProjectsImportPage firstPage = wizard.getFirstPage();
		
		firstPage.setRootDirectory(wpath);
		firstPage.selectAllProjects();
		firstPage.copyProjectsIntoWorkspace(false);
		wizard.finish();		
	}

	private void openFTLFileInEditor() {
		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();

		
		new DefaultTreeItem(prj, "ftl", "welcome.ftl").doubleClick();
		new TextEditor("welcome.ftl");
		
		log.step("Open outline view and check freemarker elements there");
		OutlineView ov = new OutlineView();
		ov.open();
		
		Collection<TreeItem> outlineElements = ov.outlineElements();
		
		List<String> list = new ArrayList<String>();
		for (TreeItem i : outlineElements) {
			list.add(i.getText());
		}
		
		assertTrue(list.contains("user"));
		assertTrue(list.contains("latestProduct.name"));
		
	    // https://issues.jboss.org/browse/JBIDE-11287
		// remove comment when this jira is fixed
		//assertTrue(list.contains("latestProduct.url"));		
	}

	private void emptyErrorLog() {
		
		LogView elv = new LogView();
		elv.open();
		//new ContextMenu("Delete Log").select();
		new WaitWhile(new JobIsRunning());
	}

	private void checkErrorLog() {
		LogView el = new LogView();
		List<LogMessage> errorMessages = el.getErrorMessages();
		for (LogMessage lm : errorMessages) { 
			log.info(lm.getMessage());
		}
	}

	// Unused now due to the Eclipse bug 
	@SuppressWarnings("unused")
	private void checkFreemMarkerOutput() {
		
		String outputExpected = "";
		String rpath = getResourceAbsolutePath(
				Activator.PLUGIN_ID, "resources/fm-output.txt");
		try {
			outputExpected = readTextFileToString(rpath);
			log.info("------------------------------------------");
			log.info(outputExpected);
			log.info("------------------------------------------");
		} catch (IOException e) {
			log.error(e.getMessage());
			new RuntimeException("Unable to read from resource");
		}

		ConsoleView cv = new ConsoleView();
		cv.clearConsole();
		
		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();

		new DefaultTreeItem(prj,"src","org.jboss.tools.freemarker.testprj","FMTest.java");
		new ContextMenu("Run As","Java Application").select();

		new WaitWhile(new ShellWithTextIsActive("Progress Information"));
		
		cv.open();
		String consoleText = cv.getConsoleText();
		
		assertTrue("Output equal check",consoleText.equals(outputExpected));
	}

	private String readTextFileToString(String filePath) throws IOException {
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

	@AfterClass
	public static void aterClass() {
		// wait for all jobs
		new WaitWhile(new JobIsRunning());
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
	public String getWorkspaceAbsolutePath() {
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
	public void copyFilesBinaryRecursively(File fromLocation,
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
	 * Copies binary file originalFile to location toLocation
	 * 
	 * @param originalFile
	 * @param toLocation
	 * @throws IOException
	 */
	public void copyFilesBinary(File originalFile, File toLocation)
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
}
