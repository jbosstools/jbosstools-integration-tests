package org.jboss.tools.openshift.ui.bot.test.explorer;

import java.util.StringTokenizer;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftUI;
import org.jboss.tools.openshift.ui.bot.util.TestProperties;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.Test;

public class EmbedCartrides extends SWTTestExt {
	SWTBotTreeItem account;

	@Test
	public void canEmbeddCartriges() {

		// open OpenShift Explorer
		SWTBotView openshiftExplorer = open
				.viewOpen(OpenShiftUI.Explorer.iView);

		// get 1st account in OpenShift Explorer
		SWTBotTreeItem account = openshiftExplorer.bot().tree().getAllItems()[0]
				.doubleClick(); // expand the account

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_20S, TIME_1S);

		assertTrue(account.getItems().length > 0);

		// click on 'Embedd cartridges'
		account.getItems()[0]
				.contextMenu(OpenShiftUI.Labels.EDIT_CARTRIDGES).click();

		bot.waitForShell("");

		SWTBotTable cartridgeTable = bot.tableInGroup("Embeddable Cartridges");

		selectCartridges(cartridgeTable);

		bot.button(IDELabel.Button.FINISH).click();

		bot.waitForShell("Embedded Cartridges", TIME_60S * 3);
		bot.button(IDELabel.Button.OK).click();

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_20S, TIME_1S);
	}

	/*
	 * Jenkins cartridge not supported yet
	 */
	private void selectCartridges(SWTBotTable cartridgeTable) {

		StringTokenizer tokenizer = new StringTokenizer(
				TestProperties.get("openshift.jbossapp.cartridges"), ";");

		while (tokenizer.hasMoreTokens()) {

			String cartridge = tokenizer.nextToken();

			if (cartridge.equals("mysql")) {
				cartridgeTable.getTableItem(OpenShiftUI.Cartridge.MYSQL)
						.toggleCheck();
			}
			if (cartridge.equals("phpmyadmin")) {
				cartridgeTable.getTableItem(OpenShiftUI.Cartridge.PHPMYADMIN)
						.toggleCheck();
			}
			if (cartridge.equals("cron")) {
				cartridgeTable.getTableItem(OpenShiftUI.Cartridge.CRON)
						.toggleCheck();
			}
			if (cartridge.equals("postgresql")) {
				cartridgeTable.getTableItem(OpenShiftUI.Cartridge.POSTGRESQL)
						.toggleCheck();
			}
			if (cartridge.equals("mongodb")) {
				cartridgeTable.getTableItem(OpenShiftUI.Cartridge.MONGODB)
						.toggleCheck();
			}
			if (cartridge.equals("rockmongo")) {
				cartridgeTable.getTableItem(OpenShiftUI.Cartridge.ROCKMONGO)
						.toggleCheck();
			}
			if (cartridge.equals("metrics")) {
				cartridgeTable.getTableItem(OpenShiftUI.Cartridge.METRICS)
						.toggleCheck();
			}
		}
	}

}
