package org.jboss.tools.openshift.ui.bot.test.domain;

import org.junit.After;
import org.junit.Test;

/**
 * 
 * Delete multiple domains on a connection. Assert that a connection contains 
 * multiple domains - it used CreateMultipleDomain before calling this test.
 * 
 * @author mlabuda@redhat.com
 *
 */
public class DeleteMultipleDomain {

	@Test
	public void canDestroyDomain() {
		DeleteDomain.destroyDomain(true);
	}
	
	@After
	public void recreateDomain() {
		CreateDomain.createDomain(false, false);
	}
	
}
