package org.jboss.tools.openshift.ui.bot.test.explorer;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftUI;
import org.jboss.tools.openshift.ui.bot.util.TestProperties;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.Test;

public class DeleteApp extends SWTTestExt {

	@Test
	public void canDeleteApplication() {

		SWTBotView openshiftExplorer = open.viewOpen(OpenShiftUI.Explorer.iView);

		SWTBotTreeItem account = openshiftExplorer
				.bot()
				.tree()
				.getAllItems()[0] // get 1st account in OpenShift Explorer
				.doubleClick(); // collapse account

		account.getNode(0).contextMenu(OpenShiftUI.Labels.EXPLORER_DELETE_APP)
				.click();

		bot.waitForShell(OpenShiftUI.Shell.DELETE_APP);

		bot.button(IDELabel.Button.OK).click();
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);

		assertTrue("Application still present in the OpenShift Explorer!",
				account.getItems().length == 0);

		projectExplorer.show();
		projectExplorer.bot().tree(0).contextMenu("Delete").click();

		bot.waitForShell("Delete Resources");
		bot.checkBox().select();
		bot.button(IDELabel.Button.OK).click();

		assertFalse("The project still exists!",
				projectExplorer.existsResource(TestProperties
						.get("openshift.jbossapp.name")));
		
	}
}
