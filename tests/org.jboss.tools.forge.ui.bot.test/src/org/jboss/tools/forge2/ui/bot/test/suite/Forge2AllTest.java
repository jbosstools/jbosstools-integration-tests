package org.jboss.tools.forge2.ui.bot.test.suite;


import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.forge2.ui.bot.wizard.test.ConstraintSetupWizardTest;
import org.jboss.tools.forge2.ui.bot.wizard.test.FacesSetupWizardTest;
import org.jboss.tools.forge2.ui.bot.wizard.test.JPASetupWizardTest;
import org.jboss.tools.forge2.ui.bot.wizard.test.ProjectNewWizardTest;
import org.jboss.tools.forge2.ui.bot.wizard.test.ServletSetupWizardTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;


/**
 * 
 * @author psrna
 *
 */
@SuiteClasses({
	ProjectNewWizardTest.class,
	JPASetupWizardTest.class,
	ServletSetupWizardTest.class,
	ConstraintSetupWizardTest.class,
	FacesSetupWizardTest.class
})
@RunWith(RedDeerSuite.class)
public class Forge2AllTest {
}
