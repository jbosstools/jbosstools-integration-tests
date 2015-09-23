package org.jboss.tools.openshift.ui.bot.test.domain;

import org.jboss.tools.openshift.ui.bot.test.util.Datastore;
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
		ID201NewDomainTest.createDomain(Datastore.USERNAME, Datastore.SERVER, Datastore.SECOND_DOMAIN);
	}
}
