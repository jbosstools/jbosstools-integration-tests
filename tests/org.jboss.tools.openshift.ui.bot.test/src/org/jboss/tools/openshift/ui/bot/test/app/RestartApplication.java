package org.jboss.tools.openshift.ui.bot.test.app;

import java.util.Date;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.openshift.ui.bot.test.OpenShiftBotTest;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftUI;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@Require(clearWorkspace = true)
public class RestartApplication extends OpenShiftBotTest {

	private final String DYI_APP = "diyapp" + new Date().getTime();

	@Before
	public void createDYIApp() {
		createOpenShiftApplication(DYI_APP, OpenShiftUI.AppType.DIY);
	}

	@Test
	public void canRestartDYIApplicationViaExplorer() {
		SWTBotView openshiftExplorer = open
				.viewOpen(OpenShiftUI.Explorer.iView);

		SWTBotTreeItem account = openshiftExplorer.bot().tree().getAllItems()[0]
				.doubleClick(); // expand account

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);

		// refresh explorer
		openshiftExplorer.bot().tree().getAllItems()[0].contextMenu(
				OpenShiftUI.Labels.REFRESH).click();
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);

		account.getNode(DYI_APP + " " + OpenShiftUI.AppTypeOld.DIY)
				.contextMenu(OpenShiftUI.Labels.EXPLORER_RESTART_APP).click();
		
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 3, TIME_1S);
		
		openshiftExplorer = open
				.viewOpen(OpenShiftUI.Explorer.iView);

		account = openshiftExplorer.bot().tree().getAllItems()[0]
				.doubleClick(); // expand account

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);

		account.getNode(0).contextMenu(OpenShiftUI.Labels.BROWSER).click();

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);
		assertTrue(open.viewOpen(OpenShiftUI.Explorer.iView).getTitle()
				.contains("OpenShift"));
	}

	@After
	public void deleteDIYApp() {
		deleteOpenShiftApplication(DYI_APP, OpenShiftUI.AppTypeOld.DIY);
	}
}
