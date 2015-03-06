package org.jboss.tools.hibernate.reddeer.common;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;

/**
 * File helper
 * @author Jiri Peterka
 *
 */
public class FileHelper {
				
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
	 * Copies binary file originalFile to location toLocation
	 * 
	 * @param originalFile source file location
	 * @param toLocation destination dir 
	 * @throws IOException exception
	 */
	public static void copyFilesBinary(String srcFile, String destDir)
			throws IOException {
		
		File originalFile = new File(srcFile);
		File toLocation = new File(destDir);
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
