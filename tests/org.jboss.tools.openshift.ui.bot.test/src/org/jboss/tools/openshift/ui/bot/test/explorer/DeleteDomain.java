package org.jboss.tools.openshift.ui.bot.test.explorer;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftUI;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.junit.Test;

public class DeleteDomain extends SWTTestExt {

	@Test
	public void canDestroyDomain() throws InterruptedException {

		SWTBotView openshiftExplorer = open.viewOpen(OpenShiftUI.Explorer.iView);

		// refresh first
		openshiftExplorer.bot().tree()
				.getAllItems()[0]
				.contextMenu(OpenShiftUI.Labels.REFRESH).click();

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 3, TIME_1S);
		
		// delete
		openshiftExplorer.bot().tree()
				.getAllItems()[0]
				.contextMenu(OpenShiftUI.Labels.EXPLORER_DELETE_DOMAIN).click();

		bot.checkBox().select();
		bot.button("OK").click();

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 4, TIME_1S);		
	}
}
