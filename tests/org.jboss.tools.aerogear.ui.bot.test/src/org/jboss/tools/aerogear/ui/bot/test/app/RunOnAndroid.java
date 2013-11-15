package org.jboss.tools.aerogear.ui.bot.test.app;

import org.jboss.tools.aerogear.ui.bot.test.AerogearBotTest;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.junit.Test;

@Require(clearWorkspace = true)
public class RunOnAndroid extends AerogearBotTest {
	@Test
	public void canRunOnAndroidEmulator() {
		projectExplorer.selectProject(CORDOVA_APP);

		runTreeItemInAndroidEmulator(bot.tree().expandNode(CORDOVA_APP));
	}

	@Test
	public void canRunOnAndroidDevice() {
		projectExplorer.selectProject(CORDOVA_APP);

		runTreeItemInAndroidEmulator(bot.tree().expandNode(CORDOVA_APP));
	}

}
