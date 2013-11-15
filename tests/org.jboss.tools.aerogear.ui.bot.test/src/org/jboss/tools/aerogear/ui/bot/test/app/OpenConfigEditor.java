package org.jboss.tools.aerogear.ui.bot.test.app;

import org.jboss.tools.aerogear.ui.bot.test.AerogearBotTest;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.junit.Test;

@Require(clearWorkspace = true)
public class OpenConfigEditor extends AerogearBotTest {
	@Test
	public void canOpenConfigXmlEditor() {
		projectExplorer.selectProject(CORDOVA_APP);

		openInConfigEditor(bot.tree().expandNode(CORDOVA_APP));

		assertTrue(bot.activeEditor().getTitle()
				.equalsIgnoreCase("Cordova Configuration Editor"));
	}
	@Override
	public void tearDown() {
		// close config editor before deleting project
		bot.activeEditor().close();
		super.tearDown();
	}

}
