package org.jboss.tools.openshift.ui.bot.test.explorer;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftUI;
import org.jboss.tools.openshift.ui.bot.util.TestProperties;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.Test;

public class CreateAdapter extends SWTTestExt {

	@Test
	public void canCreateAdapter() {

		SWTBotView openshiftExplorer = open.viewOpen(OpenShiftUI.Explorer.iView);

		SWTBotTreeItem account = openshiftExplorer
				.bot()
				.tree()
				.getAllItems()[0] // get 1st account in OpenShift Explorer
				.doubleClick(); // expand account

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);
		
		account.getNode(0).contextMenu(OpenShiftUI.Labels.EXPLORER_ADAPTER)
				.click();

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);
		bot.waitForShell(OpenShiftUI.Shell.ADAPTER);
		
		bot.button(IDELabel.Button.NEXT).click();
		bot.button(IDELabel.Button.FINISH).click();
		
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);

		// TODO workaround for: https://issues.jboss.org/browse/JBIDE-13560
		assertTrue(servers.serverExists(TestProperties.get("openshift.jbossapp.name")
				+ " at Openshift (1)"));

		log.info("*** OpenShift SWTBot Tests: OpenShift Server Adapter created. ***");
	}

}
