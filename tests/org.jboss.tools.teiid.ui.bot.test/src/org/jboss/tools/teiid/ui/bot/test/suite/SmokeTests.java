package org.jboss.tools.teiid.ui.bot.test.suite;

import org.jboss.tools.teiid.ui.bot.test.ImportWizardTest;
import org.jboss.tools.teiid.ui.bot.test.ModelWizardTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite for smoke teiid bot tests
 * 
 * @author apodhrad
 * 
 */
@SuiteClasses({
	ImportWizardTest.class,
	ModelWizardTest.class
})
@RunWith(TeiidSuite.class)
public class SmokeTests {

}
