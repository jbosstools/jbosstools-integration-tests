package org.teiid.designer.ui.bot.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import org.teiid.designer.ui.bot.test.ImportWizardTest;
import org.teiid.designer.ui.bot.test.ModelWizardTest;

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
