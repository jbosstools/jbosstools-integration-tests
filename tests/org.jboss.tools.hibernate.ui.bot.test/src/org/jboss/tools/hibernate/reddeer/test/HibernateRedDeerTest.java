package org.jboss.tools.hibernate.reddeer.test;

import org.jboss.reddeer.core.interceptor.SyncInterceptorManager;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.workbench.handler.EditorHandler;
import org.jboss.tools.hibernate.reddeer.database.DatabaseUtils;
import org.jboss.tools.hibernate.reddeer.importer.ProjectImporter;
import org.jboss.tools.hibernate.reddeer.interceptor.ErrorLogInterceptor;
import org.jboss.tools.hibernate.ui.bot.test.Activator;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class HibernateRedDeerTest {
	
	private SyncInterceptorManager sim = SyncInterceptorManager.getInstance();
	private final String LOG_INTERCEPTOR = "error-log-interceptor";
	private static String dbFolder = System.getProperty("database.path");
	
	@BeforeClass
	public static void beforeClass() {
		DatabaseUtils.runSakilaDB(dbFolder);
	}

	@AfterClass
	public static void afterClass() {
		DatabaseUtils.stopSakilaDB();
	}

	public HibernateRedDeerTest() {
		super();
	
		EditorHandler.getInstance().closeAll(false);
		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		pe.deleteAllProjects();
		
		sim.unregisterAll();
		String enabled = System.getProperty("hibernate.reddeer.errorLogInterceptor");
		if (enabled != null) 
		  sim.register(LOG_INTERCEPTOR, new ErrorLogInterceptor());		
	}
	
	public static void importProject(String prjName) {
		ProjectImporter.importProjectWithoutErrors(Activator.PLUGIN_ID, prjName);
	}
}	


