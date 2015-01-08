package org.jboss.tools.forge2.ui.bot.test.suite;


import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.forge2.ui.bot.wizard.test.JPASetupWizardTest;
import org.jboss.tools.forge2.ui.bot.wizard.test.ProjectNewWizardTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;


/**
 * 
 * @author psrna
 *
 */
@SuiteClasses({
	//Wizard Tests
	ProjectNewWizardTest.class,
	JPASetupWizardTest.class
})
@RunWith(RedDeerSuite.class)
public class Forge2AllTest {
}
