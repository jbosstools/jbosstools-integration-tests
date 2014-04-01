package org.jboss.tools.openshift.ui.bot.test.domain;

import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.junit.Before;
import org.junit.Test;


/**
 * 
 * @author mlabuda
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
