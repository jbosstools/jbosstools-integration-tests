package org.jboss.tools.openshift.ui.bot.test.cartridge;

import java.util.Date;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
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
public class EmbedCartridges extends OpenShiftBotTest {

	private final String DYI_APP = "dapp" + new Date().getTime();

	@Before
	public void createDYIApp() {
		createOpenShiftApplication(DYI_APP, OpenShiftUI.AppType.DIY);
	}

	@Test
	public void canEmbedCartridge() {
		embedCartrige(OpenShiftUI.Cartridge.CRON);
		
		//embedCartrige(OpenShiftUI.Cartridge.MYSQL);
		
		//embedCartrige(OpenShiftUI.Cartridge.POSTGRESQL);
	}

	
	private void embedCartrige(String cartridge) {

		// open OpenShift Explorer
		SWTBotView openshiftExplorer = open
				.viewOpen(OpenShiftUI.Explorer.iView);

		// get 1st account in OpenShift Explorer
		SWTBotTreeItem account = openshiftExplorer.bot().tree().getAllItems()[0]
				.doubleClick(); // expand the account

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_20S, TIME_1S);

		assertTrue(account.getItems().length > 0);

		// click on 'Embedd cartridges'
		account.getNode(0).doubleClick();
		
		account.getNode(0).getNode(0).contextMenu(OpenShiftUI.Labels.EDIT_CARTRIDGES)
				.click();
		
		bot.waitForShell(OpenShiftUI.Shell.EDIT_CARTRIDGES);

		bot.shell(OpenShiftUI.Shell.EDIT_CARTRIDGES).activate();
		SWTBotTable cartridgeTable = bot.tableInGroup("Embeddable Cartridges");

		cartridgeTable.getTableItem(cartridge).toggleCheck();

		bot.button(IDELabel.Button.FINISH).click();

		bot.waitForShell("Embedded Cartridges", TIME_60S * 3);
		bot.button(IDELabel.Button.OK).click();

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_20S, TIME_1S);
	}

	@After
	public void deleteDIYApp() {
		deleteOpenShiftApplication(DYI_APP, OpenShiftUI.AppType.DIY);
	}

}
