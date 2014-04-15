package org.jboss.tools.openshift.ui.bot.test.domain;

import org.junit.After;
import org.junit.Test;

/**
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
