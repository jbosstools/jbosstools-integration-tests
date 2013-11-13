package org.jboss.tools.openshift.ui.bot.test.explorer;

import java.util.Date;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.openshift.ui.bot.test.OpenShiftBotTest;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftExplorerView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftUI;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for tailing remote files, port forwarding, opening web browser & displaying env.variables.
 * To prevent creation of OpenShift application for each test, all tests are included in one test method.
 * 
 * @author sbunciak, mlabuda
 *
 */
@Require(clearWorkspace = true)
public class OpenShiftDebugFeatures extends OpenShiftBotTest {

	private final String DYI_APP = "diyapp" + new Date().getTime();

	@Before
	public void createDYIApp() {
		createOpenShiftApplication(DYI_APP, OpenShiftUI.AppType.DIY);
	}
	
	@Test
	public void testDebugFeatures() {
		canTailFiles();
		canForwardPorts();
		canOpenWebBrowser();
		canShowEnvVariables();
		canCreateEnvVariable();
	}
	
	public void canTailFiles() {
		openDebugFeature(OpenShiftUI.Labels.EXPLORER_TAIL_FILES);
		bot.sleep(5000);
		
		bot.waitForShell(OpenShiftUI.Shell.TAIL_FILES);
		bot.sleep(1000);
		bot.button(IDELabel.Button.FINISH).click();
		
		// invoke another action for more log
		openDebugFeature("Show in Web Browser");
		
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);
		console.show();
		
		assertFalse("Remote console should not be empty!", console
				.getConsoleText().isEmpty());
	}

	public void canForwardPorts() {
		openDebugFeature(OpenShiftUI.Labels.EXPLORER_PORTS);

		bot.waitForShell(OpenShiftUI.Shell.PORTS);
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);
		bot.sleep(TIME_5S);
		bot.button("Start All").click(); 
		
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);
		bot.button("OK").click();

		assertFalse("Console should not be empty!", console.getConsoleText()
				.isEmpty());

		open.viewOpen(OpenShiftUI.Explorer.iView);
		openDebugFeature(OpenShiftUI.Labels.EXPLORER_PORTS);
		
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);
		bot.waitForShell(OpenShiftUI.Shell.PORTS);

		bot.button("Stop All").click();
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);
		bot.button("OK").click();
	}

	public void canOpenWebBrowser() {
		openDebugFeature("Show in Web Browser");

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);
		assertTrue(open.viewOpen(OpenShiftUI.Explorer.iView).getTitle()
				.contains("OpenShift"));
	}

	public void canShowEnvVariables() {
		openDebugFeature(OpenShiftUI.Labels.EXPLORER_ENV_VAR);

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);

		assertFalse("Console should not be empty!", console.getConsoleText()
				.isEmpty());
	}

	public void canCreateEnvVariable() {
		openDebugFeature("Edit Environment Variables...");
		
		bot.waitForShell("Select Domain").activate();
		bot.sleep(1000);
		
		bot.button("Add...").click();
		bot.waitForShell("Edit Environment variable").activate();
		
		bot.text(0).setText("myVariable");
		bot.text(1).setText("myValue");
		bot.sleep(TIME_1S);
		
		bot.button("OK").click();
		bot.sleep(3000);
		
		bot.button("Finish").click();
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S, TIME_1S);
	}
	
	
	private void openDebugFeature(String label) {
		SWTBotView openshiftExplorer = open
				.viewOpen(OpenShiftUI.Explorer.iView);

		SWTBotTreeItem account = openshiftExplorer.bot().tree().getAllItems()[0];
		account.contextMenu("Refresh").click();
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 3, TIME_1S);
		
		account.expand();
		bot.sleep(3000);
				
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 3, TIME_1S);
		
		account.getNode(0).expand();
		bot.sleep(3000);
				
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 3, TIME_1S);
		
		account.getNode(0).getNode(0).select().contextMenu(label).click();
	}
	
	@After
	public void deleteDIYApp() {
		deleteOpenShiftApplication(DYI_APP, OpenShiftUI.AppType.DIY);
	}
	

}
