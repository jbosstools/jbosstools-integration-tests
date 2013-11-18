package org.jboss.tools.aerogear.ui.bot.test.app;

import org.jboss.tools.aerogear.ui.bot.test.AerogearBotTest;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.junit.Test;

@Require(clearWorkspace = true)
public class CreateHybridApplication extends AerogearBotTest {
	@Test
	public void canCreateHTMLHybridApplication() {
    // Project is created within setup method
		assertTrue(projectExplorer.existsResource(CORDOVA_PROJECT_NAME));
	}
}
