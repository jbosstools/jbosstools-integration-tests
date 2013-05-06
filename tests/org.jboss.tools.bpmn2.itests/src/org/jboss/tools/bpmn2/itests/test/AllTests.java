package org.jboss.tools.bpmn2.itests.test;

import junit.framework.TestSuite;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.bpmn2.itests.test.editor.*;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * 
 * This is BPMN2 swtbot test case for JBoss Tools.
 * 
 * @author Marek Baluch
 * 
 */
@RunWith(RedDeerSuite.class)
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
})
public class AllTests extends TestSuite {
	
}