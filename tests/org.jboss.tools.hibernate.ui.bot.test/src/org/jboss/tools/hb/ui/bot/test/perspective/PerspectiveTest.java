package org.jboss.tools.hb.ui.bot.test.perspective;

import org.jboss.tools.hibernate.reddeer.console.HibernateConfigurationView;
import org.jboss.tools.hibernate.reddeer.test.HibernateRedDeerTest;
import org.junit.Test;

/**
 * Hibernate perspective ui bot test
 * - Hibernate perspective and Hibernate views exists
 * 
 * @author jpeterka
 * 
 */
public class PerspectiveTest extends HibernateRedDeerTest {
		
	@Test
	public void openPerspectiveElements() {
		HibernateConfigurationView v = new HibernateConfigurationView();
		v.open();
	}
}
