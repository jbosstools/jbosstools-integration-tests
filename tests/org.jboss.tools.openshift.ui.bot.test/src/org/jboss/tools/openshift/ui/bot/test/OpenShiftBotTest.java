package org.jboss.tools.openshift.ui.bot.test;

import org.apache.log4j.Level;
import org.apache.log4j.Priority;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftUI;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.view.ConsoleView;
import org.jboss.tools.ui.bot.ext.view.ServersView;

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
		
		// reopen OpenShift Explorer
		// workaround - missing menu
		bot.viewByTitle("OpenShift Explorer").close();
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_5S, TIME_1S);
		SWTBotView openshiftExplorer = open.viewOpen(OpenShiftUI.Explorer.iView);

		SWTBotTreeItem account = openshiftExplorer.bot().tree().getAllItems()[0].doubleClick();
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_20S, TIME_1S);
		account.getNode(0).select();

		bot.sleep(1000);
		account.getNode(0).contextMenu("New").click().menu("Application...").click();
		bot.sleep(3000);
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S, TIME_1S);

		// fill app info
		bot.textInGroup("New application", 0).setText(APP_NAME);

		log.info("*** OpenShift SWTBot Tests: Application name set. ***");
		bot.sleep(TIME_5S);

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
				.button(IDELabel.Button.NEXT)));
		bot.button(IDELabel.Button.NEXT).click();
		
		bot.waitUntil(Conditions.widgetIsEnabled(bot
				.button(IDELabel.Button.FINISH)));
		bot.button(IDELabel.Button.FINISH).click();

		log.info("*** OpenShift SWTBot Tests: Application creation started. ***");

		SWTBotShell shell;
		
		// workaround for 'embedding DYI' 
		// scaling are now ok
		if (APP_TYPE.equals(OpenShiftUI.AppType.DIY)) {
			shell = bot.waitForShell("Embedded Cartridges", 300);
			if (shell != null) {
				shell.activate();
				bot.sleep(3000);
				bot.button(IDELabel.Button.OK).click();
			}
		}
		
		// with random names it will appear everytime
		shell = bot.waitForShell("Question", 450);
		if (shell == null) {
			throw new OpenShiftBotTestException(
					"Waiting for creation of application " + APP_NAME + " "
							+ APP_TYPE + " timeouted.");
		}
		
		bot.sleep(TIME_5S);
		shell.activate();
		bot.sleep(TIME_5S);
		shell.bot().button(IDELabel.Button.YES).click();
		bot.sleep(TIME_1S);
		
		// publish changes
		if (createAdapter) {
			bot.waitForShell("Publish " + APP_NAME + "?", 450);
			bot.shell("Publish " + APP_NAME + "?").activate();
			bot.sleep(TIME_5S);
			bot.button(IDELabel.Button.YES).click();
			
			bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 6, TIME_1S);

			// check successful build after auto git push
			ConsoleView consoleView = new ConsoleView();
			assertTrue(!consoleView.getConsoleText().isEmpty());

			if (!consoleView.getConsoleText().contains("BUILD SUCCESS")
					&& APP_TYPE.contains("JBoss")) {
				log.error("*** OpenShift SWTBot Tests: OpenShift build output does not contain succesfull maven build. ***");
			}

			ServersView serverView = new ServersView();
			serverView.show();
			bot.sleep(TIME_1S);
			
			// Republish if there are local changes - workaround - it doesnt work all the time bcs.
			// sometimes after republish still persist Republish instead of Synchronized in lable
			for (int i=0; i < 4; i++) {
				if (serverView.serverExists(APP_NAME + " at OpenShift  [Started, Republish]")) {
					serverView.findServerByName(APP_NAME + " at OpenShift  [Started, Republish]").contextMenu("Publish").click();
					bot.sleep(TIME_1S);
					
					bot.waitForShell("Publish " + APP_NAME + "?", 450);
					bot.shell("Publish " + APP_NAME + "?").activate();
					bot.sleep(TIME_5S);
					bot.button(IDELabel.Button.YES).click();
					
					bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 3, TIME_1S  * 3);
					bot.sleep(TIME_5S);
				} else {
					break;
				}
			}
			
			serverView.show();
			assertTrue(serverView.serverExists(APP_NAME + " at OpenShift  [Started, Synchronized]"));
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

		SWTBotTreeItem account = openshiftExplorer.bot().tree().getAllItems()[0].expand();

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);

		account.getNode(0).expand();
		bot.sleep(TIME_5S);
		account.getNode(0).getNode(APP_NAME + " " + APP_TYPE).select().
				contextMenu(OpenShiftUI.Labels.EXPLORER_DELETE_APP).click();
		
		bot.waitForShell(OpenShiftUI.Shell.DELETE_APP);
		bot.sleep(TIME_5S);
				
		bot.shell(OpenShiftUI.Shell.DELETE_APP).activate();
		bot.button("OK").click();
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);

		assertTrue("Application still present in the OpenShift Explorer!",
				account.getNode(0).getItems().length == 0);

		projectExplorer.show();
 		// manually refresh all projects in project explorer so they can be removed with swt bot
		for(SWTBotTreeItem item : projectExplorer.bot().tree().getAllItems()) {
			item.contextMenu("Refresh").click();
			bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);
		} 
		
		projectExplorer.deleteAllProjects();
		assertFalse("The project still exists!",
				projectExplorer.bot().tree().getAllItems().length > 0);

		servers.show();
		servers.deleteServer(APP_NAME + " at OpenShift");
	}

}
