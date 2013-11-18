package org.jboss.tools.aerogear.ui.bot.test.app;

import org.jboss.tools.aerogear.ui.bot.test.AerogearBotTest;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.junit.Test;

@Require(clearWorkspace = true)
public class OpenConfigEditor extends AerogearBotTest {
	@Test
	public void canOpenConfigXmlEditor() {
		projectExplorer.selectProject(CORDOVA_PROJECT_NAME);

		openInConfigEditor(bot.tree().expandNode(CORDOVA_PROJECT_NAME));

		assertTrue(bot.activeEditor().getTitle()
				.equalsIgnoreCase(CORDOVA_APP_NAME));
	}
	@Override
	public void tearDown() {
		// close config editor before deleting project
		bot.activeEditor().close();
		super.tearDown();
	}

}
