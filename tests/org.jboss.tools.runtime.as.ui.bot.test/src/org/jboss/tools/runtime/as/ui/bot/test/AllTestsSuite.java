package org.jboss.tools.runtime.as.ui.bot.test;

import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.tools.runtime.as.ui.bot.test.archives.ArchivesServerIntegrationTests;
import org.jboss.tools.runtime.as.ui.bot.test.archives.DeployingArchiveTest;
import org.jboss.tools.runtime.as.ui.bot.test.archives.VariousProjectsArchiving;
import org.jboss.tools.runtime.as.ui.bot.test.download.InvalidCredentialProductDownloadTest;
import org.jboss.tools.runtime.as.ui.bot.test.parametized.server.ServerRuntimesTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(RedDeerSuite.class)
@Suite.SuiteClasses({ 
	InvalidCredentialProductDownloadTest.class, 
	ServerRuntimesTest.class,
	// SeamRuntimesTest.class // failing due to trust store issues??
	VariousProjectsArchiving.class,
	DeployingArchiveTest.class,
})
public class AllTestsSuite {

}
