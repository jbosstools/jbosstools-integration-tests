package org.jboss.tools.openshift.ui.bot.test.explorer;

import java.util.Date;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.openshift.ui.bot.test.OpenShiftBotTest;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftUI;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@Require(clearWorkspace = true)
public class OpenShiftDebugFeatures extends OpenShiftBotTest {

	private final String DYI_APP = "diyapp" + new Date().getTime();

	@Before
	public void createDYIApp() {
		createOpenShiftApplication(DYI_APP, OpenShiftUI.AppType.DIY);
	}

	@Test
	public void canTailFiles() {
		SWTBotView openshiftExplorer = open
				.viewOpen(OpenShiftUI.Explorer.iView);

		SWTBotTreeItem account = openshiftExplorer.bot().tree().getAllItems()[0] // get
																					// 1st
																					// account
																					// in
																					// OpenShift
																					// Explorer
				.doubleClick(); // expand account

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);

		account.getNode(0).contextMenu(OpenShiftUI.Labels.EXPLORER_TAIL_FILES)
				.click();

		bot.waitForShell(OpenShiftUI.Shell.TAIL_FILES);
		bot.button(IDELabel.Button.FINISH).click();
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);

		assertFalse("Remote console should not be empty!", console
				.getConsoleText().isEmpty());
	}

	@Test
	public void canForwardPorts() {
		SWTBotView openshiftExplorer = open
				.viewOpen(OpenShiftUI.Explorer.iView);

		SWTBotTreeItem account = openshiftExplorer.bot().tree().getAllItems()[0] // get
																					// 1st
																					// account
																					// in
																					// OpenShift
																					// Explorer
				.doubleClick(); // expand account

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);

		account.getNode(0).contextMenu(OpenShiftUI.Labels.EXPLORER_PORTS)
				.click();

		bot.waitForShell(OpenShiftUI.Shell.PORTS);
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);
		bot.sleep(TIME_5S);
		bot.button("Start All").click(); // TODO

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);
		bot.button("OK").click();

		assertFalse("Console should not be empty!", console.getConsoleText()
				.isEmpty());

		open.viewOpen(OpenShiftUI.Explorer.iView);
		account.getNode(0).contextMenu(OpenShiftUI.Labels.EXPLORER_PORTS)
				.click();

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);
		bot.waitForShell(OpenShiftUI.Shell.PORTS);

		bot.button("Stop All").click();
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);
		bot.button("OK").click();
	}

	@Test
	public void canOpenWebBrowser() {
		SWTBotView openshiftExplorer = open
				.viewOpen(OpenShiftUI.Explorer.iView);

		SWTBotTreeItem account = openshiftExplorer.bot().tree().getAllItems()[0] // get
																					// 1st
																					// account
																					// in
																					// OpenShift
																					// Explorer
				.doubleClick(); // expand account

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);

		account.getNode(0).contextMenu(OpenShiftUI.Labels.BROWSER).click();

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);
		assertTrue(open.viewOpen(OpenShiftUI.Explorer.iView).getTitle()
				.contains("OpenShift"));
	}

	@Test
	public void canShowEnvVariables() {
		SWTBotView openshiftExplorer = open
				.viewOpen(OpenShiftUI.Explorer.iView);

		SWTBotTreeItem account = openshiftExplorer.bot().tree().getAllItems()[0] // get
																					// 1st
																					// account
																					// in
																					// OpenShift
																					// Explorer
				.doubleClick(); // expand account

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);

		account.getNode(0).contextMenu(OpenShiftUI.Labels.EXPLORER_ENV_VAR)
				.click();

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);

		assertFalse("Console should not be empty!", console.getConsoleText()
				.isEmpty());
	}

	@After
	public void deleteDIYApp() {
		deleteOpenShiftApplication(DYI_APP, OpenShiftUI.AppTypeOld.DIY);
	}

}
