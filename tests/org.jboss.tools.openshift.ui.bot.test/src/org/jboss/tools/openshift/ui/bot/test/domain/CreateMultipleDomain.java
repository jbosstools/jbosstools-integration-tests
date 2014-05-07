package org.jboss.tools.openshift.ui.bot.test.domain;

import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.junit.Before;
import org.junit.Test;


/**
 * 
 * Test capabilities of creating multiple domains on a OpenShift connection.
 * Assert, before using, that the given OpenShift server supports multiple domains
 * and that you are using account supporting this feature (an account has to have
 * appropriate plan).
 * 
 * @author mlabuda@redhat.com
 *
 */
public class CreateMultipleDomain {

	@Before
	public void waitNoJobs() {
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}
	
	@Test
	public void createMoreDomains() {
		CreateDomain.createDomain(true, true);
	}
	
}
