package org.jboss.tools.hibernate.reddeer.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.handler.WorkbenchHandler;
import org.jboss.reddeer.swt.interceptor.SyncInterceptorManager;
import org.jboss.tools.hibernate.reddeer.factory.ResourceFactory;
import org.jboss.tools.hibernate.reddeer.importer.ProjectImporter;
import org.jboss.tools.hibernate.reddeer.interceptor.ErrorLogInterceptor;
import org.jboss.tools.hibernate.ui.bot.test.Activator;

public class HibernateRedDeerTest {
	
	protected Properties p,links;
	private SyncInterceptorManager sim = SyncInterceptorManager.getInstance();
	private final String LOG_INTERCEPTOR = "error-log-interceptor";
	
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
		
		//assureResources("H2_DRIVER");
		
		WorkbenchHandler.getInstance().closeAllEditors();
		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.deleteAllProjects();
		
		sim.unregisterAll();
		String enabled = System.getProperty("hibernate.reddeer.errorLogInterceptor");
		if (enabled != null) 
		  sim.register(LOG_INTERCEPTOR, new ErrorLogInterceptor());		
	}
	
	@SuppressWarnings("unused")
	private void assureResources(String string) {
		String sourceVar = string + "_SOURCE";
		String targetVar = string + "_TARGET";
				
		String sourceVal = links.getProperty(sourceVar);
		String[] separated = sourceVal.split("/");
		String fileName = separated[separated.length - 1];
		String targetVal = links.getProperty(targetVar)  + File.separator + fileName;
		
		ResourceFactory.assureResource(Activator.PLUGIN_ID, sourceVal,targetVal);
	}

	public static void importProject(String prjName) {
		ProjectImporter.importProjectWithoutErrors(Activator.PLUGIN_ID, prjName);
	}
}	


