package org.jboss.tools.freemarker.ui.bot.test;

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
import org.jboss.reddeer.common.wait.WaitWhile;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.eclipse.ui.views.log.LogMessage;
import org.jboss.reddeer.eclipse.ui.views.log.LogView;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.jboss.reddeer.workbench.ui.dialogs.WorkbenchPreferenceDialog;

/**
 * Freemarker test parent
 * @author Jiri Peterka
 *
 */
public class FreemarkerTest {
		
	private static final Logger log = Logger.getLogger(FreeMarkerEditorTest.class);
	
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
	public void importTestProject() {
		
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
	 * Remove freemarker test project
	 */
	public void removeTestProject(String prj) {
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
