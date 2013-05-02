package org.jboss.tools.bpmn2.itests.test;

import org.jboss.tools.bpmn2.itests.test.editor.*;
import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
//import org.jboss.tools.ui.bot.ext.SWTTestExt;

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
//	ProjectWizardTest.class
//	ProcessWizardTest.class
//	Bpmn2ModelWizardTest.class
//	GenericBpmn2ModelWizardTest.class
//	JBpmProcessWizardTest.class
//	ConstructTest.class
	ModelingSmokeTest.class
//	SubProcessTest.class
})
public class AllTests extends SWTTestExt {

}