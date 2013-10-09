package org.jboss.tools.aerogear.ui.bot.test.app;

import java.util.Date;

import org.jboss.tools.aerogear.ui.bot.test.AerogearBotTest;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
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
    final String cordovaSimProcessName = "CordovaSimRunner";
    int countBrowserSimmProcesses = SWTUtilExt.countJavaProcess(cordovaSimProcessName);
    // this also asserts that CordovaSim runs without error within JBT
    runTreeItemWithCordovaSim(bot.tree().expandNode(CORDOVA_APP));
    assertTrue("No new CordovaSimm process was started",countBrowserSimmProcesses + 1 == SWTUtilExt.countJavaProcess(cordovaSimProcessName));
    // currently there is no way how to close CordovaSim within running JBT
    // CordovaSim is automatically closed when JBT are
	}

	@After
	public void deleteHybridApplication() {
		// close CordovaSim before deleting project
		closeCordovaSim();
		
		projectExplorer.deleteProject(CORDOVA_APP, true);
	}

}
