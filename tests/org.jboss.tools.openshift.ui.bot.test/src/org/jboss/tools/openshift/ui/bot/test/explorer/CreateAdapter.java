package org.jboss.tools.openshift.ui.bot.test.explorer;

import java.util.Date;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
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
public class CreateAdapter extends OpenShiftBotTest {

	private final String DYI_APP = "diyapp" + new Date().getTime();
	
	@Before
	public void createDIYApp() {
		createOpenShiftApplicationWithoutAdapter(DYI_APP, OpenShiftUI.AppType.DIY);
	}
	
	@Test
	public void canCreateAdapterViaServers() {
		SWTBotView openshiftExplorer = open
				.viewOpen(OpenShiftUI.Explorer.iView);

		// refresh explorer
		openshiftExplorer.bot().tree().getAllItems()[0].contextMenu(
				OpenShiftUI.Labels.REFRESH).click();
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);
		

		SWTBotTreeItem account = openshiftExplorer.bot().tree().getAllItems()[0];
		account.contextMenu("Refresh").click();
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 3, TIME_1S);
		
		account.expand();
		bot.sleep(3000);
				
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 3, TIME_1S);
		
		account.getNode(0).expand();
		bot.sleep(3000);
				
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 3, TIME_1S);
		
		account.getNode(0).getNode(0).select().contextMenu(OpenShiftUI.Labels.EXPLORER_ADAPTER).click();
		bot.sleep(TIME_1S);
				
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
		deleteOpenShiftApplication(DYI_APP, OpenShiftUI.AppType.DIY);
	}
}
