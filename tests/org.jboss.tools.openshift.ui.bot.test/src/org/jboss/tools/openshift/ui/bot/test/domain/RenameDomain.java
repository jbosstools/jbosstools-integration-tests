package org.jboss.tools.openshift.ui.bot.test.domain;

import java.util.Random;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftUI;
import org.jboss.tools.openshift.ui.bot.util.TestProperties;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.junit.Test;

@Require(clearWorkspace = true)
public class RenameDomain extends SWTTestExt {

	@Test
	public void canRenameDomain() {
		renameDomain();
	}
	
	private void renameDomain() {
		// open OpenShift Explorer
		SWTBotView openshiftExplorer = open
				.viewOpen(OpenShiftUI.Explorer.iView);

		openshiftExplorer.bot().tree().getAllItems()[0].contextMenu(OpenShiftUI.Labels.MANAGE_DOMAINS).click();

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_20S, TIME_5S);
		
		bot.shell("Domains").activate();
		
		String oldDomainName = bot.table(0).getTableItem(0).getText();
		String newDomainName = oldDomainName.substring(0, oldDomainName.length() - 2);
		
		bot.table(0).getTableItem(0).select();
		bot.button("Edit...").click();
		bot.sleep(TIME_1S);
		bot.text(0).setText(newDomainName);
		bot.sleep(TIME_5S);
		bot.button("Finish").click();
		
		bot.waitUntil(Conditions.shellCloses(bot.activeShell()), TIME_60S + TIME_30S);
		log.info("*** OpenShift SWTBot Tests: Domain renamed. ***");

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_20S, TIME_1S);
		
		openshiftExplorer.bot().tree().getAllItems()[0].contextMenu(
				OpenShiftUI.Labels.REFRESH).click();

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 3, TIME_1S);
	}

}
