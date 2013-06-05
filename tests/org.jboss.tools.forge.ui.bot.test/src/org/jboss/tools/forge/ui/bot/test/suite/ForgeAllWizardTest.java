package org.jboss.tools.forge.ui.bot.test.suite;

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
	ProjectWizardTest.class
})
@RunWith(RequirementAwareSuite.class)
public class ForgeAllWizardTest {
}
