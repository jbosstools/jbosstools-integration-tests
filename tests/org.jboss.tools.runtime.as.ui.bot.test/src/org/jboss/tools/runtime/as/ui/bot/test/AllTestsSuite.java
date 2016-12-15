package org.jboss.tools.runtime.as.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.runtime.as.ui.bot.test.detector.RuntimeDuplications;
import org.jboss.tools.runtime.as.ui.bot.test.download.InvalidCredentialProductDownloadTest;
import org.jboss.tools.runtime.as.ui.bot.test.parametized.ParameterizedDownloadRuntimeTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({
		RuntimeDuplications.class,
		InvalidCredentialProductDownloadTest.class,
		ParameterizedDownloadRuntimeTest.class
})
public class AllTestsSuite {

}
