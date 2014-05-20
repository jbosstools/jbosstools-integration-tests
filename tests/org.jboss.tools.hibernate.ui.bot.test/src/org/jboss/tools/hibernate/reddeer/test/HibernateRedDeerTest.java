package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.ExternalProjectImportWizardDialog;
import org.jboss.reddeer.eclipse.ui.wizards.datatransfer.WizardProjectsImportPage;
import org.jboss.reddeer.swt.test.RedDeerTest;
import org.jboss.tools.hibernate.ui.bot.testcase.Activator;

public class HibernateRedDeerTest {
	
	Properties p;
	
	public HibernateRedDeerTest() {
		super();
		p = new Properties();
		String path = getResourceAbsolutePath(Activator.PLUGIN_ID, "resources/h2_settings.properties");
		try {
			p.load(new FileInputStream(path));
		} catch (IOException e) {
			new RuntimeException("Cannot read property file");
		}
		
	}
	
	public void importProject(String prjName) {
		System.out.println("Import projects");
		ExternalProjectImportWizardDialog w = new ExternalProjectImportWizardDialog();
		w.open();
		WizardProjectsImportPage p1 = w.getFirstPage();
		p1.setRootDirectory(getResourceAbsolutePath(Activator.PLUGIN_ID, "resources/prj"));
		p1.copyProjectsIntoWorkspace(true);
		p1.deselectAllProjects();
		p1.selectProjects(prjName);
		w.finish();
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

}
