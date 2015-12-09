package org.jboss.tools.eclipsecs.ui.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.problems.Problem;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.eclipse.ui.problems.matcher.ProblemsTypeMatcher;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.tools.eclipsecs.ui.test.view.MarkerStatsView;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Eclipse Checkstyle test
 * @author Jiri Peterka
 *
 */
@RunWith(RedDeerSuite.class)
public class CheckstyleTest {
		
	private static final Logger log = Logger.getLogger(CheckstyleTest.class);
		
	private final String PROJECT_NAME = "cstest";
	
	@Test 
	 public void checkStyleTest()
	 {
		log.step("Import tests project");
		importTestProject();
		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(PROJECT_NAME);
		
		new DefaultTreeItem(PROJECT_NAME,"src","org.jbds.cs","CSTestClass.java").select();
		
		new ContextMenu("Checkstyle","Check Code with Checkstyle").select();
		
		new WaitWhile(new JobIsRunning());
		
		ProblemsView pv = new ProblemsView();
		pv.open();
		
		List<Problem> problems = pv.getProblems(ProblemType.WARNING, new ProblemsTypeMatcher("Checkstyle Problem"));
		assertTrue("There must be Checkstyle Problems reported", problems.size() > 0);
	 }
	
	@Test 
	 public void checkStyleViolationsTest()
	 {
		log.step("Import tests project");
		importTestProject();
		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.selectProjects(PROJECT_NAME);
		
		new DefaultTreeItem(PROJECT_NAME,"src","org.jbds.cs","CSTestClass.java").select();
		
		new ContextMenu("Checkstyle","Check Code with Checkstyle").select();
		
		new WaitWhile(new JobIsRunning());
		
		MarkerStatsView v = new MarkerStatsView();
		v.open();
		
		assertTrue("There must be Checkstyle Violations reported", v.getItemCount() > 0);
	 }
	
	
	/**
	 * Imports checkstyle test project
	 */
	public static void importTestProject() {
		
		ExternalProjectImportWizardDialog wizard = new ExternalProjectImportWizardDialog();		
		wizard.open();

		String rpath = getResourceAbsolutePath(
				Activator.PLUGIN_ID, "resources/checkstyle/prj");
		String wpath = getWorkspaceAbsolutePath();
		File rfile = new File(rpath);
		File wfile = new File(wpath);
		try {
			copyFilesBinaryRecursively(rfile, wfile, null);
		} catch (IOException e) {
			fail("Unable to copy checkstyle test project");
		}
		
		WizardProjectsImportPage firstPage = new WizardProjectsImportPage();
		
		firstPage.setRootDirectory(wpath);
		firstPage.selectAllProjects();
		firstPage.copyProjectsIntoWorkspace(false);
		wizard.finish();		
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
	 * Remove checkstyle test project
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
	
	@After 
	public void clean() {			
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.getProject(PROJECT_NAME).delete(true);
	}

	
	}
