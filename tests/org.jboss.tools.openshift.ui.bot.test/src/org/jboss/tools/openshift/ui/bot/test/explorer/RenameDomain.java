package org.jboss.tools.openshift.ui.bot.test.explorer;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftUI;
import org.jboss.tools.openshift.ui.bot.util.TestProperties;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.Test;

public class RenameDomain extends SWTTestExt {

	@Test
	public void canRenameDomain() {

		SWTBotView openshiftExplorer = open
				.viewOpen(OpenShiftUI.Explorer.iView);

		openshiftExplorer
					.bot()
					.tree()
					.getAllItems()[0] // get 1st account in OpenShift Explorer
					.contextMenu(OpenShiftUI.Labels.EXPLORER_CREATE_EDIT_DOMAIN)
					.click(); // click on 'Create or Edit Domain'

		bot.waitForShell(OpenShiftUI.Shell.EDIT_DOMAIN);

		SWTBotText domainText = bot.text(0);

		assertTrue("Domain should be set correctly at this stage!", domainText
				.getText().equals(TestProperties.get("openshift.domain")));

		domainText.setText(TestProperties.get("openshift.domain.new"));

		log.info("*** OpenShift SWTBot Tests: Domain name re-set. ***");

		bot.button(IDELabel.Button.FINISH).click();
		bot.waitUntil(Conditions.shellCloses(bot.activeShell()), TIME_60S
				+ TIME_30S);

		log.info("*** OpenShift SWTBot Tests: Domain renamed. ***");

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_20S, TIME_1S);
	}

}
