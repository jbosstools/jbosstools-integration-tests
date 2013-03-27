package org.jboss.tools.bpmn2.ui.bot.test.suite;

import org.jboss.tools.bpmn2.ui.bot.test.legacy.ProjectWizardTest;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.jboss.tools.ui.bot.ext.SWTTestExt;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * 
 * This is BPMN2 swtbot test case for JBoss Tools.
 * 
 * @author Marek Baluch
 * 
 */
@RunWith(RequirementAwareSuite.class)
@SuiteClasses({
	ProjectWizardTest.class
})
public class Bpmn2AllBotTests extends SWTTestExt {

	// TBD
	
}