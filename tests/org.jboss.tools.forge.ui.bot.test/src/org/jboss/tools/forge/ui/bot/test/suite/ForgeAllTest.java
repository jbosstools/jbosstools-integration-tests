package org.jboss.tools.forge.ui.bot.test.suite;

import org.jboss.tools.forge.ui.bot.console.test.CommandTest;
import org.jboss.tools.forge.ui.bot.console.test.EntityTest;
import org.jboss.tools.forge.ui.bot.console.test.ForgeViewTest;
import org.jboss.tools.forge.ui.bot.console.test.InstallPluginTest;
import org.jboss.tools.forge.ui.bot.console.test.PersistenceTest;
import org.jboss.tools.forge.ui.bot.console.test.PreferencesTest;
import org.jboss.tools.forge.ui.bot.console.test.ProjectTest;
import org.jboss.tools.forge.ui.bot.console.test.ScaffoldingTest;
import org.jboss.tools.forge.ui.bot.wizard.test.ProjectWizardTest;

import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;


/**
 * 
 * @author psrna
 *
 */
@SuiteClasses({
	//console tests
	ForgeViewTest.class,
	ProjectTest.class,
	PersistenceTest.class,
	EntityTest.class,
	InstallPluginTest.class,
	ScaffoldingTest.class,
	CommandTest.class,
	PreferencesTest.class,
	//wizard tests
	ProjectWizardTest.class
})
@RunWith(RequirementAwareSuite.class)
public class ForgeAllTest {
}
