package org.jboss.tools.forge2.ui.bot.test.suite;


import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.forge2.ui.bot.wizard.test.ConstraintSetupWizardTest;
import org.jboss.tools.forge2.ui.bot.wizard.test.EJBSetupWizardTest;
import org.jboss.tools.forge2.ui.bot.wizard.test.FacesSetupWizardTest;
import org.jboss.tools.forge2.ui.bot.wizard.test.JPAEntityWizardTest;
import org.jboss.tools.forge2.ui.bot.wizard.test.JPAFieldWizardTest;
import org.jboss.tools.forge2.ui.bot.wizard.test.JPASetupWizardTest;
import org.jboss.tools.forge2.ui.bot.wizard.test.ProjectNewWizardTest;
import org.jboss.tools.forge2.ui.bot.wizard.test.RESTSetupWizardTest;
import org.jboss.tools.forge2.ui.bot.wizard.test.ScaffoldSetupWizardTest;
import org.jboss.tools.forge2.ui.bot.wizard.test.ScaffoldWizardTest;
import org.jboss.tools.forge2.ui.bot.wizard.test.ServletSetupWizardTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;


/**
 * @author Pavol Srna
 * Forge Smoke Test Suite
 */
@SuiteClasses({
	ProjectNewWizardTest.class,
	JPASetupWizardTest.class,
	ServletSetupWizardTest.class,
	ConstraintSetupWizardTest.class,
	FacesSetupWizardTest.class,
	RESTSetupWizardTest.class,
	EJBSetupWizardTest.class,
	JPAEntityWizardTest.class,
	JPAFieldWizardTest.class,
	ScaffoldSetupWizardTest.class,
	ScaffoldWizardTest.class
})
@RunWith(RedDeerSuite.class)
public class SmokeSuite {
}
