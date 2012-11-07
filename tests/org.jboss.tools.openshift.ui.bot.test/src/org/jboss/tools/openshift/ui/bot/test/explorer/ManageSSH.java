package org.jboss.tools.openshift.ui.bot.test.explorer;

import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.util.List;

import org.eclipse.swtbot.eclipse.finder.finders.CommandFinder;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotCommand;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.jboss.tools.openshift.ui.bot.test.Activator;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftUI;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.Test;

public class ManageSSH extends SWTTestExt {

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

		bot.waitForShell(OpenShiftUI.Shell.NEW_SSH);
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_20S, TIME_1S);

		bot.button("Refresh...").click();
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_20S, TIME_1S);

		// delete all keys
		for (int i = 0; i < bot.table().rowCount(); i++) {
			bot.table().getTableItem(i).select();
			bot.button("Remove...").click();
			bot.waitForShell("");
			bot.button(IDELabel.Button.OK).click();
			bot.waitWhile(new NonSystemJobRunsCondition(), TIME_20S, TIME_1S);
		}

		// Path to the ssh key pair
		File sshPrivateKey = new File(
				new File(Activator.location, "resources"), "jbt");
		File sshPublicKey = new File(new File(Activator.location, "resources"),
				"jbt.pub");

		assertTrue("SSH Key pair doesn't exist!", sshPrivateKey.exists()
				&& sshPublicKey.exists());

		// add existing
		bot.button("Add Existing...").click();
		bot.waitForShell("");
		bot.text(0).setText("swtbotkey");
		bot.text(1).setText(sshPublicKey.getAbsolutePath());
		// add to ssh prefs
		bot.link(0).click("SSH2 Preferences");
		bot.waitForShell(IDELabel.Shell.PREFERENCES);
		bot.text(2).setText(
				bot.text(1).getText() + "," + sshPrivateKey.getAbsolutePath());

		bot.button(IDELabel.Button.OK).click();

		bot.button(IDELabel.Button.FINISH).click();
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S, TIME_1S);

		bot.button(IDELabel.Button.OK).click();
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S, TIME_1S);
	}

}