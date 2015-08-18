package org.jboss.tools.aerogear.ui.bot.test.rhmap;

import static org.junit.Assert.assertTrue;

import org.jboss.tools.aerogear.ui.bot.test.AerogearBotTest;
import org.jboss.tools.aerogear.ui.bot.test.FeedHenryBotTest;
import org.jboss.tools.browsersim.reddeer.BrowserSimHandler;
import org.junit.BeforeClass;
import org.junit.Test;

public class RunFHCordovaApp extends FeedHenryBotTest {

	@BeforeClass
	public static void importApp() {
		importApp(FH_PROJECT, FH_APP_NAME);
	}

	@Test
	public void testFHAppCanRunWithCordovaSim() {

		final String cordovaSimProcessName = "CordovaSimRunner";
		int countBrowserSimmProcesses = AerogearBotTest.countJavaProcess(cordovaSimProcessName);
		runWithRemoteFeedHenryServer(FH_APP_NAME);
		assertTrue("No new CordovaSimm process was started",
				countBrowserSimmProcesses + 1 == AerogearBotTest.countJavaProcess(cordovaSimProcessName));
		BrowserSimHandler.closeAllRunningInstances();
	}
	
}
