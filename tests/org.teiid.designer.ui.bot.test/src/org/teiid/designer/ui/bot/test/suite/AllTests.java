package org.teiid.designer.ui.bot.test.suite;

import org.jboss.tools.ui.bot.ext.RequirementAwareSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import org.teiid.designer.ui.bot.test.ImportWizardTest;

/**
 * Test suite for all teiid bot tests
 * 
 * @author apodhrad
 * 
 */
@SuiteClasses({ ImportWizardTest.class,
// ModelWizardTest.class,
// TopDownWsdlTest.class
// VirtualGroupTutorialTest.class
// TeiidSourceInSeamTest.class
// TeiidSourceInHibernateToolsTest.class
})
@RunWith(RequirementAwareSuite.class)
public class AllTests {

}
