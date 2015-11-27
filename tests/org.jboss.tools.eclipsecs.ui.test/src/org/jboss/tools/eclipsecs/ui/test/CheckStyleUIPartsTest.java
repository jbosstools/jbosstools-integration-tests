package org.jboss.tools.eclipsecs.ui.test;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.ui.perspectives.JavaPerspective;
import org.jboss.reddeer.workbench.impl.view.WorkbenchView;
import org.jboss.tools.eclipsecs.ui.test.view.GraphStatsView;
import org.jboss.tools.eclipsecs.ui.test.view.MarkerStatsView;
import org.junit.After;
import org.junit.Test;

/**
 * Tests Checkstyle UI Parts
 * @author Jiri Peterka
 *
 */
public class CheckStyleUIPartsTest {

	
	private static final Logger log = Logger.getLogger(CheckStyleUIPartsTest.class);
	
	@Test
	/**
	 * Tests Checkstyle views
	 * - Violations view
	 * - Violations chart view
	 */
	public void testCheckstyleViews() {
		log.step("Check Checkstyle violations view");
		checkView(new MarkerStatsView());
		log.step("Check Checkstyle violations graph view");
		checkView(new GraphStatsView());
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
	
	@After
	public void after() {
		JavaPerspective p = new JavaPerspective();
		p.open();
		p.reset();
	}
}
