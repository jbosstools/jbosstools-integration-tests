package org.jboss.tools.openshift.ui.bot.test.domain;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftUI;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.junit.After;
import org.junit.Test;

@Require(clearWorkspace = true)
public class DeleteDomain extends SWTTestExt {

	@Test
	public void canDestroyDomain() {
		destroyDomain(false);
	}
	
	@After
	public void recreateDomain() {
		CreateDomain.createDomain(false, false);
	}
	
	private void destroyDomain(boolean multipleDomain) {
		SWTBotView openshiftExplorer = open
				.viewOpen(OpenShiftUI.Explorer.iView);

		// refresh first
		openshiftExplorer.bot().tree().getAllItems()[0].contextMenu(
				OpenShiftUI.Labels.REFRESH).click();

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 3, TIME_1S);

		openshiftExplorer.bot().tree().getAllItems()[0].contextMenu(
				OpenShiftUI.Labels.MANAGE_DOMAINS).click();
		
		bot.shell("Domains").activate();

		removeDomain();
		
		if (multipleDomain) {
			removeDomain();
		}
		
		assertTrue("*** Domain has not been deleted. ***",bot.table(0).rowCount() == 0);
		
		bot.button("OK").click();
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 4, TIME_1S);		
	}
	
	private void removeDomain() {
		bot.table(0).getTableItem(0).select();		
		bot.button("Remove...").click();
		bot.checkBox(0).select();
		bot.button("OK").click();
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 4, TIME_1S);
	}

}
