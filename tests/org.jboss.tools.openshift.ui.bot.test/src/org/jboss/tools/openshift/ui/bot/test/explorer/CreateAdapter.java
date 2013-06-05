package org.jboss.tools.openshift.ui.bot.test.explorer;

import java.util.Date;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftUI;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@Require(clearWorkspace = true)
public class CreateAdapter extends SWTTestExt {

	private final String DYI_APP = "diyapp" + new Date().getTime();

	@Before
	public void createDYIApp() {
		// open OpenShift Explorer
		SWTBotView openshiftExplorer = open
				.viewOpen(OpenShiftUI.Explorer.iView);

		openshiftExplorer.bot().tree().getAllItems()[0].contextMenu(
				OpenShiftUI.Labels.EXPLORER_NEW_APP).click();

		bot.waitForShell(OpenShiftUI.Shell.NEW_APP);
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_20S, TIME_1S);

		// fill app info
		bot.textInGroup("New application", 0).setText(DYI_APP);

		log.info("*** OpenShift SWTBot Tests: Application name set. ***");

		bot.comboBoxInGroup("New application").setSelection(
				OpenShiftUI.AppType.DIY);

		log.info("*** OpenShift SWTBot Tests: Application type selected. ***");

		// Do not create server adapter
		bot.button(IDELabel.Button.NEXT).click();
		bot.checkBox(1).deselect();

		bot.waitUntil(Conditions.widgetIsEnabled(bot
				.button(IDELabel.Button.FINISH)));
		bot.button(IDELabel.Button.FINISH).click();

		log.info("*** OpenShift SWTBot Tests: Application creation started. ***");

		// add to known_hosts
		bot.waitForShell("Question", TIME_60S * 4);
		bot.button(IDELabel.Button.YES).click();

		// create known_hosts since it does not exists any more
		SWTBotShell khShell = bot.waitForShell("Publish " + DYI_APP + "?");
		if (khShell != null) {
			bot.button(IDELabel.Button.YES).click();
		}

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 10, TIME_1S);
	}

	@Test
	public void canCreateAdapterViaServers() {

		SWTBotView openshiftExplorer = open
				.viewOpen(OpenShiftUI.Explorer.iView);

		SWTBotTreeItem account = openshiftExplorer.bot().tree().getAllItems()[0] 
				.doubleClick(); // expand account

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);

		account.getNode(0).contextMenu(OpenShiftUI.Labels.EXPLORER_ADAPTER)
				.click();

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);
		bot.waitForShell(OpenShiftUI.Shell.ADAPTER);

		bot.button(IDELabel.Button.NEXT).click(); 
		bot.comboBox(1).setSelection(0);
		bot.button(IDELabel.Button.FINISH).click();

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);

		servers.show();
		assertTrue(servers.serverExists(DYI_APP + " at OpenShift"));

		log.info("*** OpenShift SWTBot Tests: OpenShift Server Adapter created. ***");
	}

	@After
	public void deleteDIYApp() {

		SWTBotView openshiftExplorer = open
				.viewOpen(OpenShiftUI.Explorer.iView);

		SWTBotTreeItem account = openshiftExplorer.bot().tree().getAllItems()[0]
				.doubleClick(); // expand account

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);

		account.getNode(DYI_APP + " " + OpenShiftUI.AppTypeOld.DIY)
				.contextMenu(OpenShiftUI.Labels.EXPLORER_DELETE_APP).click();

		bot.waitForShell(OpenShiftUI.Shell.DELETE_APP);

		bot.button(IDELabel.Button.OK).click();
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);

		assertTrue("Application still present in the OpenShift Explorer!",
				account.getItems().length == 0);

		projectExplorer.show();
		projectExplorer.deleteAllProjects();

		assertFalse("The project still exists!",
				bot.tree().getAllItems().length > 0);

		servers.show();
		servers.deleteServer(DYI_APP + " at OpenShift");
	}
}
