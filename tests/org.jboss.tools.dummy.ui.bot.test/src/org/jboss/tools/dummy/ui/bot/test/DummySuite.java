package org.jboss.tools.dummy.ui.bot.test;

import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Dummy test suite is SWTBot test suite for basic jenkins slave test
 * @author Jiri Peterka
 *
 */
@RunWith(RequirementAwareSuite.class)
@SuiteClasses({DummyTest.class})
public class DummySuite {

}
