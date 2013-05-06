package org.jboss.tools.bpmn2.itests.test;

import junit.framework.TestSuite;

import org.jboss.tools.bpmn2.itests.reddeer.suite.BPMN2Suite;
import org.jboss.tools.bpmn2.itests.test.editor.*;
import org.jboss.tools.bpmn2.itests.test.editor.smoke.ModelingSmokeTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * 
 * This is BPMN2 swtbot test case for JBoss Tools.
 * 
 * @author Marek Baluch
 * 
 */
@RunWith(BPMN2Suite.class)
@SuiteClasses({
// Wizard tests
// ------------
//	ProjectWizardTest.class
//	ProcessWizardTest.class
//	Bpmn2ModelWizardTest.class
//	GenericBpmn2ModelWizardTest.class
//	JBpmProcessWizardTest.class
// Editor tests
// ------------
	ModelingSmokeTest.class
//	CallActivityTest.class
})
public class AllTests extends TestSuite {
	
}