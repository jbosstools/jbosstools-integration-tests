package org.jboss.tools.aerogear.ui.bot.test.app;

import java.util.Date;

import org.jboss.tools.aerogear.ui.bot.test.AerogearBotTest;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@Require(clearWorkspace = true)
public class OpenConfigEditor extends AerogearBotTest {

	private final String CORDOVA_APP = "Cordova_app_" + new Date().getTime();

	@Before
	public void canCreateHTMLHybridApplication() {
		createHTMLHybridMobileApplication(CORDOVA_APP, "Aerogear Cordova Test",
				"org.jboss.example.cordova");

		assertTrue(projectExplorer.existsResource(CORDOVA_APP));
	}

	@Test
	public void canOpenConfigXmlEditor() {
		projectExplorer.selectProject(CORDOVA_APP);

		openInConfigEditor(bot.tree().expandNode(CORDOVA_APP));

		assertTrue(bot.activeEditor().getTitle()
				.equalsIgnoreCase("Cordova Configuration Editor"));
	}

	@After
	public void deleteHybridApplication() {
		// close config editor before deleting project
		bot.activeEditor().close();
		
		projectExplorer.deleteProject(CORDOVA_APP, true);
	}

}
