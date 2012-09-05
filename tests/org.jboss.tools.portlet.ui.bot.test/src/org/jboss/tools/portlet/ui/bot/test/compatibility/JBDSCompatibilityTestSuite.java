package org.jboss.tools.portlet.ui.bot.test.compatibility;

import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RequirementAwareSuite.class)
@Suite.SuiteClasses({
	JBDS5vs6CompatibilityGatein.class,
})
public class JBDSCompatibilityTestSuite {

}
