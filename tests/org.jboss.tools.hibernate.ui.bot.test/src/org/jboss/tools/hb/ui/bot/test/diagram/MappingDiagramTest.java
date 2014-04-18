package org.jboss.tools.hb.ui.bot.test.diagram;

import org.jboss.tools.hibernate.reddeer.console.HibernateConfigurationView;
import org.jboss.tools.hibernate.reddeer.test.HibernateRedDeerTest;
import org.junit.Test;

/**
 * Hibernate mapping diagram ui bot test
 * 
 * @author jpeterka
 * 
 */

public class MappingDiagramTest extends HibernateRedDeerTest {
	
	final String hc = "hibernate35";

	/**
	 * Test imports projects and check if mapping diagram can be opened
	 */
	@Test
	public void showMappingDiagram() {
		importProject("/resources/prj/hibernatelib");
		importProject("/resources/prj/hibernate35");
		openDiagram();
	}

	private void openDiagram() {
		HibernateConfigurationView cv = new HibernateConfigurationView();
		cv.open();
	}
}
