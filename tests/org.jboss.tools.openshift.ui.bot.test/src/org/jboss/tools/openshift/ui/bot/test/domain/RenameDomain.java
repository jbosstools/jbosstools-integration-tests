package org.jboss.tools.openshift.ui.bot.test.domain;

import java.util.Date;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.jboss.tools.openshift.ui.bot.test.Utils;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftUI;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.Test;

@Require(clearWorkspace = true)
public class RenameDomain extends SWTTestExt {

	@Test
	public void canRenameDomain() {

		// open OpenShift Explorer
		SWTBotView openshiftExplorer = open
				.viewOpen(OpenShiftUI.Explorer.iView);

		SWTBotShell[] oldShells = bot.shells();
		openshiftExplorer.bot().tree().getAllItems()[0]
				// get 1st account in OpenShift Explorer
				.contextMenu(OpenShiftUI.Labels.EXPLORER_CREATE_EDIT_DOMAIN)
				.click(); // click on 'Create or Edit Domain'

		Utils.getNewShell(oldShells, bot.shells()).activate();
		SWTBotText domainText = bot.text(0);

		assertFalse("Domain should be set at this stage!", domainText.getText().equals(""));

		domainText.setText(Long.toString(new Date().getTime()));

		log.info("*** OpenShift SWTBot Tests: Domain name re-set. ***");

		bot.button(IDELabel.Button.FINISH).click();
		bot.waitUntil(Conditions.shellCloses(bot.activeShell()), TIME_60S
				+ TIME_30S);

		log.info("*** OpenShift SWTBot Tests: Domain renamed. ***");

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_20S, TIME_1S);
		
		openshiftExplorer.bot().tree().getAllItems()[0].contextMenu(
				OpenShiftUI.Labels.REFRESH).click();

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 3, TIME_1S);
	}

}
