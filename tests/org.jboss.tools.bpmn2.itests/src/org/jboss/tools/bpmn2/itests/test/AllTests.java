package org.jboss.tools.bpmn2.itests.test;

import junit.framework.TestSuite;

import org.jboss.tools.bpmn2.itests.reddeer.suite.BPMN2Suite;
import org.jboss.tools.bpmn2.itests.test.editor.*;
import org.jboss.tools.bpmn2.itests.test.editor.smoke.*;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * 
 * @author Marek Baluch
 */
@RunWith(BPMN2Suite.class)
@SuiteClasses({
// Wizard tests
// ------------
//	ProjectWizardTest.class,
//	ProcessWizardTest.class,
//	Bpmn2ModelWizardTest.class,
//	GenericBpmn2ModelWizardTest.class,
//	JBpmProcessWizardTest.class,
// Editor tests
// ------------
//	ModelingSmokeTest.class,
//	CallActivityTest.class,
//	AdHocProcessTest.class,
//	AdHocSubProcessTest.class,
//	AssociationTest.class,
//	BooleanStructureReferenceTest.class
//	BusinessRuleTaskTest.class,
// TODO tests
// ----------------
//	BoundaryConditionalEventOnTaskTest.class,
//	CallActivityByNameTest.class,
//	CompensateEndEventTest.class
})
public class AllTests extends TestSuite {
	
}