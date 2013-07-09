package org.jboss.tools.aerogear.ui.bot.test.app;

import java.util.Date;

import org.jboss.tools.aerogear.ui.bot.test.AerogearBotTest;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@Require(clearWorkspace = true, perspective="JBoss")
public class RunWithCordovaSim extends AerogearBotTest {

	private String CORDOVA_APP = "Cordova_app_" + new Date().getTime();

	@Before
	public void canCreateHybridApplication() {
		createHTMLHybridMobileApplication(CORDOVA_APP, "Aerogear Cordova Test",
				"org.jboss.example.cordova");
	}

	@Test
	public void canRunWithCordovaSim() {
		projectExplorer.selectProject(CORDOVA_APP);

		runTreeItemWithCordovaSim(bot.tree().expandNode(CORDOVA_APP));
	}

	@After
	public void deleteHybridApplication() {
		// close CordovaSim before deleting project
		closeCordovaSim();
		
		projectExplorer.deleteProject(CORDOVA_APP, true);
	}

}
