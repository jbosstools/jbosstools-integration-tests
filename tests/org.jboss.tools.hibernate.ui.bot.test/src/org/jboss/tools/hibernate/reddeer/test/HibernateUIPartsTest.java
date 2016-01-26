package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.*;

import org.jboss.reddeer.common.logging.Logger;
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

	
	private static final Logger log = Logger.getLogger(HibernateUIPartsTest.class);
	
	@After
	public void after() {
		JavaPerspective p = new JavaPerspective();
		p.open();
		p.reset();
	}
	
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
		log.step("Check Hibernate Console Configurations view");
		checkView(new HibernateConfigurationView());
		log.step("Check Dynamic SQL Preview View");
		checkView(new DynamicSQLPreviewView());
		log.step("Check Query Page Tab View");
		checkView(new QueryPageTabView());
		log.step("Check Query Parameters View");
		checkView(new QueryParametersView());
	}
	
	/**
	 * Tests hibernate perspective
	 */
	@Test
	public void testHibernatePerspective() {
		log.step("Check Hibernate perspective");
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
		log.step("Open view");
		view.open();
		log.step("Maximize view");
		view.maximize();
		log.step("Restore view");
		view.restore();
		log.step("Minimize view");
		view.minimize();
		log.step("Restore view");
		view.restore();
		log.step("Close view");
		view.close();		
	}
	
	
}
