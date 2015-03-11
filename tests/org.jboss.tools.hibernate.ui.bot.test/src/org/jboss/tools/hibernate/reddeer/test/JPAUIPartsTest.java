package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.*;

import org.jboss.reddeer.eclipse.ui.perspectives.JPAPerspective;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.hibernate.reddeer.console.HibernateConfigurationView;
import org.jboss.tools.hibernate.reddeer.perspective.HibernatePerspective;
import org.jboss.tools.hibernate.reddeer.view.DynamicSQLPreviewView;
import org.jboss.tools.hibernate.reddeer.view.JPADetailsView;
import org.jboss.tools.hibernate.reddeer.view.JPAStructureView;
import org.jboss.tools.hibernate.reddeer.view.QueryPageTabView;
import org.jboss.tools.hibernate.reddeer.view.QueryParametersView;
import org.junit.After;
import org.junit.Test;

/**
 * Tests JPA UI Parts - perspective and views
 * @author Jiri Peterka
 *
 */
public class JPAUIPartsTest {

	@Test
	/**
	 * Tests JPA perspective
	 * Tests JPA views
	 * - JPA Details
	 * - JPA Structure
	 */
	public void testHibernateViews() {
		checkView(new JPADetailsView());
		checkView(new JPAStructureView());		
	}
	
	/**
	 * Tests hibernate perspective
	 */
	@Test
	public void testHibernatePerspective() {
		JPAPerspective p = new JPAPerspective();
		p.open();
		p.reset();
		
		assertTrue(p.getPerspectiveLabel().equals("JPA"));
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
