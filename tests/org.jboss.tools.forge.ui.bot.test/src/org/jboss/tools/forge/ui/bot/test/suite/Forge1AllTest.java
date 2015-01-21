package org.jboss.tools.forge.ui.bot.test.suite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.forge.ui.bot.console.test.CommandTest;
import org.jboss.tools.forge.ui.bot.console.test.EntityTest;
import org.jboss.tools.forge.ui.bot.console.test.InstallPluginTest;
import org.jboss.tools.forge.ui.bot.console.test.PersistenceTest;
import org.jboss.tools.forge.ui.bot.console.test.ProjectTest;
import org.jboss.tools.forge.ui.bot.console.test.ScaffoldingTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;


/**
 * 
 * @author psrna
 *
 */
@SuiteClasses({
	ProjectTest.class,
	PersistenceTest.class,
	EntityTest.class,
	InstallPluginTest.class,
	ScaffoldingTest.class,
	CommandTest.class,
})
@RunWith(RedDeerSuite.class)
public class Forge1AllTest {
}
