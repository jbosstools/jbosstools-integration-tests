package org.jboss.tools.openshift.ui.bot.test.explorer;

import static org.hamcrest.core.IsEqual.equalTo;

import java.util.Date;
import java.util.List;

import org.eclipse.swtbot.eclipse.finder.finders.CommandFinder;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotCommand;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftUI;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.Test;

@Require(clearWorkspace = true)
public class ManageSSH extends SWTTestExt {

	public static final String SSH_KEY_NAME = "id" + new Date().getTime();

	@Test
	public void canManageSSHKeys() {
		// open OpenShift Explorer
		SWTBotView openshiftExplorer = open
				.viewOpen(OpenShiftUI.Explorer.iView);

		openshiftExplorer.bot().tree().getAllItems()[0].select();

		/*
		 * Workaround for Command based context menus
		 */
		CommandFinder finder = new CommandFinder();

		List<SWTBotCommand> cmds = finder
				.findCommand(equalTo("Manage SSH Keys..."));

		assertTrue("No command to open SSH Key Management found!",
				!cmds.isEmpty());

		// open ssh key management dialog
		cmds.get(0).click();

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_20S, TIME_5S);
//		bot.waitForShell(OpenShiftUI.Shell.NEW_SSH);
//		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_20S, TIME_5S);

		bot.buttonInGroup("Refresh...", "SSH Public Keys").click();
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S, TIME_1S);

		// delete all keys
		while (bot.table().rowCount() > 0) {
			bot.table().getTableItem(0).select();
			bot.buttonInGroup("Remove...", "SSH Public Keys").click();
			bot.waitForShell("");
			bot.button(IDELabel.Button.OK).click();
			bot.waitWhile(new NonSystemJobRunsCondition(), TIME_20S, TIME_1S);
		}

		// create new ssh key
		bot.buttonInGroup("New...", "SSH Public Keys").click();
		bot.waitForShell("", TIME_10S);
		bot.textInGroup("New SSH Key", 0).typeText(SSH_KEY_NAME);
		bot.textInGroup("New SSH Key", 2).typeText("openshift_id");

		bot.button(IDELabel.Button.FINISH).click();
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S, TIME_1S);

		bot.button(IDELabel.Button.OK).click();
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S, TIME_1S);
	}

}