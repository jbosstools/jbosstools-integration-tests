package org.jboss.tools.openshift.ui.bot.test;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.ui.internal.ShowViewMenu;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftUI;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

/**
 * Base class for OpenShift SWTBot Tests
 * 
 * @author sbunciak
 * 
 */
public class OpenShiftBotTest extends SWTTestExt {

	protected void createOpenShiftApplication(final String APP_NAME,
			final String APP_TYPE) {

		try {
			createOpenShiftApplicationScaling(APP_NAME, APP_TYPE, false, true);
		} catch (OpenShiftBotTestException e) {
			log.error("*** OpenShift Endpoint failure. ***", e);
			System.exit(1);
		}

	}

	protected void createScaledOpenShiftApplication(final String APP_NAME,
			final String APP_TYPE) {

		try {
			createOpenShiftApplicationScaling(APP_NAME, APP_TYPE, true, true);
		} catch (OpenShiftBotTestException e) {
			log.error("*** OpenShift Endpoint failure. ***", e);
			System.exit(1);
		}
	}
	
	protected void createOpenShiftApplicationWithoutAdapter(final String APP_NAME,
			final String APP_TYPE) {

		try {
			createOpenShiftApplicationScaling(APP_NAME, APP_TYPE, false, false);
		} catch (OpenShiftBotTestException e) {
			log.error("*** OpenShift Endpoint failure. ***", e);
			System.exit(1);
		}
	}

	// assumes proper setup of SSH keys
	private void createOpenShiftApplicationScaling(final String APP_NAME,
			final String APP_TYPE, final boolean scaling, final boolean createAdapter)
			throws OpenShiftBotTestException {
		// open OpenShift Explorer
		SWTBotView openshiftExplorer = open
				.viewOpen(OpenShiftUI.Explorer.iView);

		openshiftExplorer.bot().tree().getAllItems()[0] // get 1st account in
														// OpenShift Explorer
				.contextMenu(OpenShiftUI.Labels.EXPLORER_NEW_APP).click();

		bot.waitForShell(OpenShiftUI.Shell.NEW_APP);
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_20S, TIME_1S);

		// fill app info
		bot.textInGroup("New application", 0).typeText(APP_NAME);

		log.info("*** OpenShift SWTBot Tests: Application name set. ***");
		bot.sleep(TIME_1S * 3);

		bot.comboBoxInGroup("New application").setSelection(APP_TYPE);

		if (scaling) {
			/* Enable scaling */
			bot.checkBoxInGroup("New application").select();
		}

		log.info("*** OpenShift SWTBot Tests: Application type selected. ***");
		bot.sleep(TIME_1S * 3);

		bot.waitUntil(Conditions.widgetIsEnabled(bot
				.button(IDELabel.Button.NEXT)));
		bot.button(IDELabel.Button.NEXT).click();
		
		// create server adapter?
		if (!createAdapter) {
			bot.checkBox(1).deselect();
		}
		
		bot.waitUntil(Conditions.widgetIsEnabled(bot
				.button(IDELabel.Button.FINISH)));
		bot.button(IDELabel.Button.FINISH).click();

		log.info("*** OpenShift SWTBot Tests: Application creation started. ***");

		SWTBotShell shell;
		
		// workaround for 'embedding DYI'
		if (APP_TYPE.equals(OpenShiftUI.AppType.DIY) || scaling) {
			shell = bot.waitForShell("Embedded Cartridges", 180);
			if (shell != null)
				shell.activate();
				bot.sleep(3000);
				bot.button(IDELabel.Button.OK).click();
		}
		
		// with random names it will appear everytime
		shell = bot.waitForShell("Question", 180);
		if (shell == null) {
			throw new OpenShiftBotTestException(
					"Waiting for creation of application " + APP_NAME + " "
							+ APP_TYPE + " timeouted.");
		}
		
		bot.sleep(TIME_5S);
		shell.activate();
		bot.button(IDELabel.Button.YES).click();
		
		// publish changes
		if (createAdapter) {
			bot.sleep(TIME_20S);
			bot.shell("Publish " + APP_NAME + "?").activate();
			bot.sleep(TIME_5S);
			bot.activeShell().bot().button(IDELabel.Button.YES).click();
			
			bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 10, TIME_1S);

			// check successful build after auto git push
			SWTBotView consoleView = bot.views().get(4);
			consoleView.show();
			consoleView.bot().sleep(TIME_30S);	
			
			assertTrue(!consoleView.bot().styledText().getText().isEmpty());

			if (!consoleView.bot().styledText().getText().contains("BUILD SUCCESS")
					&& APP_TYPE.contains("JBoss")) {
				log.error("*** OpenShift SWTBot Tests: OpenShift build output does not contain succesfull maven build. ***");
			}

			bot.menu("Window").menu("Show View").menu("Other...").click();
			bot.shell("Show View").activate();
			bot.sleep(3000);
			bot.tree().expandNode("Server").select("Servers");
			bot.sleep(TIME_1S);
			bot.button("OK").click();
			bot.sleep(3000);
			
			SWTBotView serverView = bot.viewByTitle("Servers");
			serverView.bot().sleep(TIME_20S);
			
			System.out.println("Text " + serverView.bot().tree().getAllItems()[0].getId());
			System.out.println("ID " + serverView.bot().tree().getAllItems()[0].getText());
			System.out.println("ToolTip " + serverView.bot().tree().getAllItems()[0].getToolTipText());
			System.out.println("Node " + serverView.bot().tree().getAllItems()[0].getNodes().get(0));
			
			
			serverView.bot().sleep(TIME_30S);
			
			assertTrue(serverView.bot().tree().select(0).getText().equals(APP_NAME + " at OpenShift"));
			log.info("*** OpenShift SWTBot Tests: OpenShift Server Adapter created. ***");
		}
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 10, TIME_1S  * 3);
	}

	protected void deleteOpenShiftApplication(final String APP_NAME,
			final String APP_TYPE) {
		SWTBotView openshiftExplorer = open
				.viewOpen(OpenShiftUI.Explorer.iView);

		// refresh first (workaround fot dissapearing label issue: JBIDE-14929 )
		openshiftExplorer.bot().tree().getAllItems()[0].contextMenu(
				OpenShiftUI.Labels.REFRESH).click();

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 3, TIME_1S);

		SWTBotTreeItem account = openshiftExplorer.bot().tree().getAllItems()[0]
				.doubleClick(); // expand account

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);

		
		account.getNode(APP_NAME + " " + APP_TYPE)
				.contextMenu(OpenShiftUI.Labels.EXPLORER_DELETE_APP).click();

		SWTBotShell[] oldShells = bot.shells();
		bot.waitForShell(OpenShiftUI.Shell.DELETE_APP);

		Utils.getNewShell(oldShells, bot.shells()).activate();
		bot.activeShell().bot().button(IDELabel.Button.OK).click();
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);

		assertTrue("Application still present in the OpenShift Explorer!",
				account.getItems().length == 0);

		projectExplorer.show();
		// manually refresh all projects in project explorer so they can be removed with swt bot
		for(SWTBotTreeItem item : projectExplorer.bot().tree().getAllItems()) {
			item.contextMenu("Refresh").click();
			bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);
		} 
		
		projectExplorer.deleteAllProjects();
		assertFalse("The project still exists!",
				bot.tree().getAllItems().length > 0);

		servers.show();
		servers.deleteServer(APP_NAME + " at OpenShift");
	}

}
