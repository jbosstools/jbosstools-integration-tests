package org.jboss.tools.openshift.reddeer.utils;

import java.io.File;
import java.io.IOException;

public class TestUtils {

	public static void cleanupGitFolder(String appname) {
		File gitDir = new File(System.getProperty("user.home") + "/git");

		boolean exists = gitDir.exists() ? true : gitDir.mkdir();

		if (exists && gitDir.isDirectory() && gitDir.listFiles().length > 0) {
			for (File file : gitDir.listFiles()) {
				if (file.getName().contains(appname))
					try {
						TestUtils.delete(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
	}

	public static void delete(File file) throws IOException {
		if (file.isDirectory() && file.list().length > 0) {
			String files[] = file.list();
			for (String tmpFile : files) {
				File fileToDelete = new File(file, tmpFile);
				delete(fileToDelete);
			}
		}
		
		file.delete();
	}

}
