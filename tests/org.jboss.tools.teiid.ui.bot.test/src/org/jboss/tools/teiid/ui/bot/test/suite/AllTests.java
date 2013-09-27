package org.jboss.tools.teiid.ui.bot.test.suite;

import org.jboss.tools.teiid.ui.bot.test.E2eAudioBooksVdbExecutionTest;
import org.jboss.tools.teiid.ui.bot.test.ImportWizardTest;
import org.jboss.tools.teiid.ui.bot.test.ModelWizardTest;
import org.jboss.tools.teiid.ui.bot.test.ServerManagementTest;
import org.jboss.tools.teiid.ui.bot.test.TopDownWsdlTest;
import org.jboss.tools.teiid.ui.bot.test.VirtualGroupTutorialTest;
import org.jboss.tools.teiid.ui.bot.test.VirtualGroupTutorialUpdatedTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite for all teiid bot tests
 * 
 * @author apodhrad
 * 
 */
@SuiteClasses({
	ImportWizardTest.class,
	ModelWizardTest.class,
	TopDownWsdlTest.class,
	VirtualGroupTutorialTest.class,
	VirtualGroupTutorialUpdatedTest.class,
	E2eAudioBooksVdbExecutionTest.class,
	ServerManagementTest.class
})
@RunWith(TeiidSuite.class)
public class AllTests {

}
