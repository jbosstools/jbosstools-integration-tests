package org.jboss.tools.openshift.ui.bot.test.domain;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftUI;
import org.jboss.tools.openshift.ui.bot.util.TestProperties;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.config.Annotations.Require;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.Test;

@Require(clearWorkspace = true)
public class DeleteDomain extends SWTTestExt {

	@Test
	public void canDestroyDomain() {

		SWTBotView openshiftExplorer = open
				.viewOpen(OpenShiftUI.Explorer.iView);

		// refresh first
		openshiftExplorer.bot().tree().getAllItems()[0].contextMenu(
				OpenShiftUI.Labels.REFRESH).click();

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 3, TIME_1S);

		// delete
		try {
			openshiftExplorer.bot().tree().getAllItems()[0].contextMenu(
					OpenShiftUI.Labels.EXPLORER_DELETE_DOMAIN).click();
		} catch (TimeoutException e) {
			log.error("Domain no longer exists.", e);
		}
		

		bot.checkBox().select();
		bot.button("OK").click();

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 4, TIME_1S);

		openshiftExplorer.bot().tree().getAllItems()[0].contextMenu(
				OpenShiftUI.Labels.REFRESH).click();

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 3, TIME_1S);
	}

	// @After
	public void recreateDomain() {
		// open OpenShift Explorer
		SWTBotView openshiftExplorer = open
				.viewOpen(OpenShiftUI.Explorer.iView);

		openshiftExplorer.bot().tree().getAllItems()[0]
				// get 1st account in OpenShift Explorer
				.contextMenu(OpenShiftUI.Labels.EXPLORER_CREATE_EDIT_DOMAIN)
				.click(); // click on 'Create or Edit Domain'

		bot.waitForShell(OpenShiftUI.Shell.CREATE_DOMAIN);

		SWTBotText domainText = bot.text(0);

		assertTrue("Domain should not be set at this stage!", domainText
				.getText().equals(""));

		domainText.setText(TestProperties.get("openshift.domain"));
		log.info("*** OpenShift SWTBot Tests: Domain name set. ***");

		SWTBotButton finishBtn = bot.button(IDELabel.Button.FINISH);

		bot.waitUntil(Conditions.widgetIsEnabled(finishBtn));
		finishBtn.click();

		// wait while the domain is being created
		bot.waitUntil(Conditions.shellCloses(bot.activeShell()), TIME_60S * 3,
				TIME_1S);
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_20S, TIME_1S);

		log.info("*** OpenShift SWTBot Tests: Domain recreated. ***");
	}
}
