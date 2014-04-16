package org.jboss.tools.mylyn.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Mylyn Test Suite
 * @author ldimaggi
 *
 */

@RunWith(RedDeerSuite.class)

@SuiteClasses({
	MylynTestJenkins.class,
	MylynTestValidate.class,
	MylynTestLocalRepo.class,
	MylynTestBzQuery.class
	})

public class MylynSuite {
	
}
