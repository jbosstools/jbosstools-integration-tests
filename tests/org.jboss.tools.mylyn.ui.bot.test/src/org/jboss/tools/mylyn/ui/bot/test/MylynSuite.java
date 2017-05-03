package org.jboss.tools.mylyn.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Mylyn Test Suite
 * @author ldimaggi
 *
 */

@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({
	MylynTestJenkins.class,
	MylynTestValidate.class,
	MylynTestLocalRepo.class,
	MylynTestBzQuery.class
	})

public class MylynSuite {
	
}
