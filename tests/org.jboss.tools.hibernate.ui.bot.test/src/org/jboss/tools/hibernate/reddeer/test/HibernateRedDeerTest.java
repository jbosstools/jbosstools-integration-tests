package org.jboss.tools.hibernate.reddeer.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.jboss.tools.hibernate.factory.ResourceFactory;
import org.jboss.tools.hibernate.reddeer.importer.ProjectImporter;
import org.jboss.tools.hibernate.ui.bot.test.Activator;

public class HibernateRedDeerTest {
	
	Properties p,links;
	
	public HibernateRedDeerTest() {
		super();
		p = new Properties();
		String path = ResourceFactory.getResourcesLocation(Activator.PLUGIN_ID, "h2_settings.properties");
		try {
			p.load(new FileInputStream(path));
		} catch (IOException e) {
			new RuntimeException("Cannot read property file " + e.toString());
		}
		
		links = new Properties();
		String linksPath = ResourceFactory.getResourcesLocation(Activator.PLUGIN_ID, "links.properties");
		try {
			links.load(new FileInputStream(linksPath));
		} catch (IOException e) {
			new RuntimeException("Cannot read property file " + e.toString());
		}
		
		assureResources("H2_DRIVER");
		
		
	}
	
	private void assureResources(String string) {
		String sourceVar = string + "_SOURCE";
		String targetVar = string + "_TARGET";
				
		String sourceVal = links.getProperty(sourceVar);
		String[] separated = sourceVal.split("/");
		String fileName = separated[separated.length - 1];
		String targetVal = links.getProperty(targetVar)  + File.separator + fileName;
		
		ResourceFactory.assureResource(Activator.PLUGIN_ID, sourceVal,targetVal);
	}

	public void importProject(String prjName) {
		ProjectImporter.importProjectWithoutErrors(Activator.PLUGIN_ID, prjName);
	}
	

}
