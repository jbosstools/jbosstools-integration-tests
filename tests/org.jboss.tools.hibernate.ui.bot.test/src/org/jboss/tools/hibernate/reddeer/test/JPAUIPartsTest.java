package org.jboss.tools.hibernate.reddeer.test;

import static org.junit.Assert.assertTrue;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.ui.perspectives.JPAPerspective;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.hibernate.reddeer.view.JPADetailsView;
import org.jboss.tools.hibernate.reddeer.view.JPAStructureView;
import org.junit.After;
import org.junit.Test;

/**
 * Tests JPA UI Parts - perspective and views
 * @author Jiri Peterka
 *
 */
public class JPAUIPartsTest {
	
	private Logger log = Logger.getLogger(this.getClass());
	
	@Test
	/**
	 * Tests JPA perspective
	 * Tests JPA views
	 * - JPA Details
	 * - JPA Structure
	 */
	public void testHibernateViews() {
		log.step("Check JPA details view");
		checkView(new JPADetailsView());
		log.step("Check JPA structure view");
		checkView(new JPAStructureView());		
	}
	
	/**
	 * Tests hibernate perspective
	 */
	@Test
	public void testHibernatePerspective() {
		log.step("Open JPA perspective");
		JPAPerspective p = new JPAPerspective();		
		p.open();
		log.step("Reset perspective");
		p.reset();
		
		assertTrue(p.getPerspectiveLabel().equals("JPA"));
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
		log.step("Minimze view");
		view.minimize();
		log.step("Restore view");
		view.restore();
		log.step("Close view");
		view.close();		
	}
	
	@After
	public void after() {
		JavaPerspective p = new JavaPerspective();
		p.open();
		p.reset();
	}	
}
