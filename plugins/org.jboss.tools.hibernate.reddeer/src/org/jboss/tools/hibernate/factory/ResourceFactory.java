package org.jboss.tools.hibernate.factory;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;

/**
 * Provides usefull resource related API for hibernate tests 
 * @author Jiri Peterka
 *
 */
public class ResourceFactory {

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
}
