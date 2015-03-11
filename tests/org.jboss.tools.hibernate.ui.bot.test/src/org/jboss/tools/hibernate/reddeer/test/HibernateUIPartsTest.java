package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.*;

import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.hibernate.reddeer.console.HibernateConfigurationView;
import org.jboss.tools.hibernate.reddeer.perspective.HibernatePerspective;
import org.jboss.tools.hibernate.reddeer.view.DynamicSQLPreviewView;
import org.jboss.tools.hibernate.reddeer.view.QueryPageTabView;
import org.jboss.tools.hibernate.reddeer.view.QueryParametersView;
import org.junit.After;
import org.junit.Test;

/**
 * Tests Hibernate UI Parts - perspective and views
 * @author Jiri Peterka
 *
 */
public class HibernateUIPartsTest {

	@Test
	/**
	 * Tests Hibernate perspective
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
	 * Tests hibernate perspective
	 */
	@Test
	public void testHibernatePerspective() {
		HibernatePerspective p = new HibernatePerspective();
		p.open();
		p.reset();
		
		assertTrue(p.getPerspectiveLabel().equals("Hibernate"));
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
	
	@After
	public void after() {
		JavaPerspective p = new JavaPerspective();
		p.open();
		p.reset();
	}
	
	
}
