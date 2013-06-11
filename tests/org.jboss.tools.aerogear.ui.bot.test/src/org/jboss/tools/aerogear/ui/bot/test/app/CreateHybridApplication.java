package org.jboss.tools.aerogear.ui.bot.test.app;

import java.util.Date;

import org.jboss.tools.aerogear.ui.bot.test.AerogearBotTest;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.junit.After;
import org.junit.Test;

@Require(clearWorkspace = true)
public class CreateHybridApplication extends AerogearBotTest {

	private final String CORDOVA_APP = "Cordova_app_" + new Date().getTime();

	@Test
	public void canCreateHTMLHybridApplication() {
		createHTMLHybridMobileApplication(CORDOVA_APP, "Aerogear Cordova Test",
				"org.jboss.example.cordova");

		assertTrue(projectExplorer.existsResource(CORDOVA_APP));
	}

	@After
	public void deleteHybridApplication() {
		projectExplorer.deleteProject(CORDOVA_APP, true);
	}

}
