package org.jboss.tools.openshift.ui.bot.test.explorer;

import java.util.Date;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
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
		openSSHShell();
		
		// if there exist some keys
		removeAllKeys();

		createNewSSHKey();

		closeSSHShell();
	}

	private void openSSHShell() {
		SWTBotView openshiftExplorer = open
				.viewOpen(OpenShiftUI.Explorer.iView);

		openshiftExplorer.bot().tree().getAllItems()[0].select();
		/* TODO WTF IT IS DOING? this hide menu item New. ContextMenu method let disappear submenus...
		 comment in SWTBotExt is very helpful...:
		"in e4, if the context menu contains submenus it appears
		 as disposed after detection, so we use the ContextMenuHelper" */
		ContextMenu menu = new ContextMenu(OpenShiftUI.Labels.MANAGE_SSH_KEYS);
		menu.select();		
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_20S, TIME_5S);
		
		bot.sleep(5000);
		bot.shell("Select Domain").activate();
		bot.sleep(2000);
		bot.buttonInGroup("Refresh...", "SSH Public Keys").click();
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S, TIME_1S);
		
	}
	
	private void createNewSSHKey() {
		bot.buttonInGroup("New...", "SSH Public Keys").click();
		bot.textInGroup("New SSH Key", 0).setText(SSH_KEY_NAME);
		bot.textInGroup("New SSH Key", 2).setText("openshift_id");

		bot.button(IDELabel.Button.FINISH).click();
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S, TIME_1S);
	}
	
	private void removeAllKeys() {
		bot.sleep(TIME_1S);
		while (bot.table(0).rowCount() > 0) {
			bot.table(0).getTableItem(0).select();	
			bot.buttonInGroup("Remove...", "SSH Public Keys").click();
			bot.button(IDELabel.Button.OK).click();
			bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S, TIME_5S);
		}
	}
	
	private void closeSSHShell() {
		bot.button(IDELabel.Button.OK).click();
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S, TIME_1S);
	}
}