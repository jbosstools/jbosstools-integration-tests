package org.jboss.tools.examples.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(RedDeerSuite.class)
@SuiteClasses({
	 InitializeExamplesImport.class,
	 ImportAndCheckExamples.class
})
public class ImportExamplesTest {
}
