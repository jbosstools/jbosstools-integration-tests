package org.jboss.tools.openshift.ui.bot.test.explorer;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftUI;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.junit.Test;

public class PortForward  extends SWTTestExt {

	@Test
	public void canForwardPorts() {
		SWTBotView openshiftExplorer = open.viewOpen(OpenShiftUI.Explorer.iView);

		SWTBotTreeItem account = openshiftExplorer
				.bot()
				.tree()
				.getAllItems()[0] // get 1st account in OpenShift Explorer
				.doubleClick(); // expand account

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);
		
		account.getNode(0).contextMenu(OpenShiftUI.Labels.EXPLORER_PORTS)
				.click();

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);
		bot.waitForShell(OpenShiftUI.Shell.PORTS);
		bot.button("Start All").click();
		
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);
		/*
		bot.button("OK").click();
		
		open.viewOpen(OpenShiftUI.WebBrowser.iView);
		bot.text().typeText("localhost:9990");
		
		*/
		bot.button("Stop All").click();
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);
		bot.button("OK").click();
	}
	
}
