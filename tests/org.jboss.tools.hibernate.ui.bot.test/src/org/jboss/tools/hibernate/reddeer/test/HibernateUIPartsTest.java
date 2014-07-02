package org.jboss.tools.hibernate.reddeer.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.hibernate.reddeer.console.HibernateConfigurationView;
import org.jboss.tools.hibernate.reddeer.view.DynamicSQLPreviewView;
import org.jboss.tools.hibernate.reddeer.view.QueryPageTabView;
import org.jboss.tools.hibernate.reddeer.view.QueryParametersView;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Tests Hibernate UI Parts - perspective and views
 * @author Jiri Peterka
 *
 */
public class HibernateUIPartsTest {

	@Test
	/**
	 * Tests Hibernate views
	 * - Hibernate Configurations
	 * - Hibernate Dynamic SQL Preview
	 * - Hibernate Query Result
	 * - Query Parameters 
	 */
	public void testHibernateViews() {
		checkView(new HibernateConfigurationView());
		checkView(new DynamicSQLPreviewView());		
		checkView(new QueryPageTabView());
		checkView(new QueryParametersView());
	}
	
	/**
	 * Check bassic view operation for given view
	 * @param given view
	 */
	private void checkView(WorkbenchView view) {
		view.open();
		view.maximize();
		view.restore();
		view.minimize();
		view.restore();
		view.close();		
	}
	
	
}
