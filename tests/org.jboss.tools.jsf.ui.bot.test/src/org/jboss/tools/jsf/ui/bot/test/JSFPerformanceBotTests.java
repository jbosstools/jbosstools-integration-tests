package org.jboss.tools.jsf.ui.bot.test;

import org.jboss.tools.jsf.ui.bot.test.smoke.CreateNewJSFProjectTest;
import org.jboss.tools.perf.test.core.swtbot.PerformanceRequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * 
 * These are all JSF SWTBot tests for JBDS.
 * 
 */
@RunWith(PerformanceRequirementAwareSuite.class)
@SuiteClasses ({
  CreateNewJSFProjectTest.class
})
public class JSFPerformanceBotTests{
}