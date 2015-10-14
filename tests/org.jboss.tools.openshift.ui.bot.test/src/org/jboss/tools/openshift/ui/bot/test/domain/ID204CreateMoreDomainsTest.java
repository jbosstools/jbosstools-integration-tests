package org.jboss.tools.openshift.ui.bot.test.domain;

import org.jboss.tools.openshift.ui.bot.test.util.DatastoreOS2;
import org.junit.Test;

/**
 * Test creation of more domains on one connection.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class ID204CreateMoreDomainsTest {
	
	@Test
	public void testCreateMoreDomains() {
		ID201NewDomainTest.createDomain(DatastoreOS2.USERNAME, DatastoreOS2.SERVER, DatastoreOS2.SECOND_DOMAIN);
	}
}
