package org.jboss.tools.hibernate.reddeer.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.jboss.tools.hibernate.factory.ResourceFactory;
import org.jboss.tools.hibernate.reddeer.importer.ProjectImporter;
import org.jboss.tools.hibernate.ui.bot.test.Activator;

public class HibernateRedDeerTest {
	
	Properties p;
	
	public HibernateRedDeerTest() {
		super();
		p = new Properties();
		String path = ResourceFactory.getResourceAbsolutePath(Activator.PLUGIN_ID, "resources/h2_settings.properties");
		try {
			p.load(new FileInputStream(path));
		} catch (IOException e) {
			new RuntimeException("Cannot read property file");
		}
		
	}
	
	public void importProject(String prjName) {
		ProjectImporter.importProjectWithoutErrors(Activator.PLUGIN_ID, prjName);
	}
	

}
