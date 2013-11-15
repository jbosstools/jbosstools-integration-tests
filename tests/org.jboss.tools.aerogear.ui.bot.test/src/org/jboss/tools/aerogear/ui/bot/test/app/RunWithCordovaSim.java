package org.jboss.tools.aerogear.ui.bot.test.app;

import org.eclipse.ui.PlatformUI;
import org.jboss.tools.aerogear.ui.bot.test.AerogearBotTest;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.SWTUtilExt;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.junit.Test;

@Require(clearWorkspace = true, perspective="JBoss")
public class RunWithCordovaSim extends AerogearBotTest {
	@Test
	public void canRunWithCordovaSim() {
		projectExplorer.selectProject(CORDOVA_APP);
    final String cordovaSimProcessName = "CordovaSimRunner";
    int countBrowserSimmProcesses = SWTUtilExt.countJavaProcess(cordovaSimProcessName);
    Object[] beforeListeners = SWTEclipseExt.getWorkbenchListeners().getListeners();
    // this also asserts that CordovaSim runs without error within JBT
    runTreeItemWithCordovaSim(bot.tree().expandNode(CORDOVA_APP));
    assertTrue("No new CordovaSimm process was started",countBrowserSimmProcesses + 1 == SWTUtilExt.countJavaProcess(cordovaSimProcessName));
    // currently there is no way how to close CordovaSim within running JBT
    // CordovaSim is automatically closed when JBT are but not when run via test
    // So invoking explicitly WorkbenchListener added by CordovaSim
    SWTEclipseExt.retainFromCurrentWorkbenchListeners(beforeListeners)
      .get(0).postShutdown(PlatformUI.getWorkbench());
	}
}
