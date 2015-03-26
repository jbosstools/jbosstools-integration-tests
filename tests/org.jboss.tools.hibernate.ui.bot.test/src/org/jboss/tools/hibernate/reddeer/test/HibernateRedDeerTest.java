package org.jboss.tools.hibernate.reddeer.test;

import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.swt.handler.WorkbenchHandler;
import org.jboss.reddeer.swt.interceptor.SyncInterceptorManager;
import org.jboss.tools.hibernate.reddeer.importer.ProjectImporter;
import org.jboss.tools.hibernate.reddeer.interceptor.ErrorLogInterceptor;
import org.jboss.tools.hibernate.ui.bot.test.Activator;

public class HibernateRedDeerTest {
	
	private SyncInterceptorManager sim = SyncInterceptorManager.getInstance();
	private final String LOG_INTERCEPTOR = "error-log-interceptor";
	
	public HibernateRedDeerTest() {
		super();
	
		WorkbenchHandler.getInstance().closeAllEditors();
		
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


