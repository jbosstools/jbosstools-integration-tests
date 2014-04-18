package org.jboss.tools.hb.ui.bot.test.perspective;

import org.jboss.tools.hibernate.reddeer.test.HibernateRedDeerTest;
import org.jboss.tools.hibernate.reddeer.view.JPADetailsView;
import org.junit.Test;

/**
 * Hibernate perspective ui bot test
 * - JPA perspective and JPA views exists
 * @author jpeterka
 * 
 */

public class JPAPerspectiveTest extends HibernateRedDeerTest {

	@Test
	public void openPerspectiveElements() {
		JPADetailsView v = new JPADetailsView();
		v.open();
	}
}
