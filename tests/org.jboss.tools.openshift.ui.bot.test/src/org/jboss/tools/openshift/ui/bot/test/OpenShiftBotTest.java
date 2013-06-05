package org.jboss.tools.openshift.ui.bot.test;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
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
			createOpenShiftApplicationScaling(APP_NAME, APP_TYPE, false);
		} catch (OpenShiftBotTestException e) {
			log.error("*** OpenShift Endpoint failure. ***", e);
			System.exit(1);
		}

	}

	protected void createScaledOpenShiftApplication(final String APP_NAME,
			final String APP_TYPE) {

		try {
			createOpenShiftApplicationScaling(APP_NAME, APP_TYPE, true);
		} catch (OpenShiftBotTestException e) {
			log.error("*** OpenShift Endpoint failure. ***", e);
			System.exit(1);
		}
	}

	// assumes proper setup of SSH keys
	private void createOpenShiftApplicationScaling(final String APP_NAME,
			final String APP_TYPE, final boolean scaling)
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
		bot.sleep(TIME_1S  * 3);
		
		bot.comboBoxInGroup("New application").setSelection(APP_TYPE);

		if (scaling) {
			/* Enable scaling */
			bot.checkBoxInGroup("New application").select();
		}

		log.info("*** OpenShift SWTBot Tests: Application type selected. ***");
		bot.sleep(TIME_1S  * 3);
		
		bot.waitUntil(Conditions.widgetIsEnabled(bot
				.button(IDELabel.Button.FINISH)));
		bot.button(IDELabel.Button.FINISH).click();

		log.info("*** OpenShift SWTBot Tests: Application creation started. ***");

		// only for the 1st time - with known_hosts deleting it will appear
		// every time
		// add to known_hosts
		// TODO: add "reachable check" - if unreachable, System.exit();
		// Waiting for application + APP_NAME
		// Keep waiting
		SWTBotShell shell = bot.waitForShell("Question", TIME_60S * 4);
		if (shell == null) {
			throw new OpenShiftBotTestException(
					"Waiting for creation of application " + APP_NAME + " "
							+ APP_TYPE + " timeouted.");
		}
		bot.button(IDELabel.Button.YES).click();

		// create known_hosts since it does not exists any more
		bot.waitForShell("Publish " + APP_NAME + "?", TIME_60S);
		bot.button(IDELabel.Button.YES).click();

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 10, TIME_1S);

		// check successful build after auto git push
		console.show();
		assertTrue(!console.getConsoleText().isEmpty());

		if (!console.getConsoleText().contains("BUILD SUCCESS")
				&& APP_TYPE.contains("JBoss")) {
			log.error("*** OpenShift SWTBot Tests: OpenShift build output does not contain succesfull maven build. ***");
		}

		servers.show();
		assertTrue(servers.serverExists(APP_NAME + " at OpenShift"));

		log.info("*** OpenShift SWTBot Tests: OpenShift Server Adapter created. ***");
	}

	protected void deleteOpenShiftApplication(final String APP_NAME,
			final String APP_TYPE) {
		SWTBotView openshiftExplorer = open
				.viewOpen(OpenShiftUI.Explorer.iView);

		SWTBotTreeItem account = openshiftExplorer.bot().tree().getAllItems()[0]
				.doubleClick(); // expand account

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);

		account.getNode(APP_NAME + " " + APP_TYPE)
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
		servers.deleteServer(APP_NAME + " at OpenShift");
	}

}
