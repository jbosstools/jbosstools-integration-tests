package org.jboss.tools.openshift.ui.bot.test.app;

import java.util.Date;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftUI;
import org.jboss.tools.openshift.ui.bot.util.TestProperties;
import org.jboss.tools.openshift.ui.bot.test.OpenShiftBotTest;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.gen.ActionItem;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.Before;
import org.junit.Test;

public class RepublishApp extends OpenShiftBotTest {

	private final String DYI_APP = "diyapp" + new Date().getTime();
	
	@Before
	public void createDYIApp() {
		createOpenShiftApplication(DYI_APP, OpenShiftUI.AppType.DIY);
	}
	
	@Test
	public void canModifyAndRepublishApp() {
		projectExplorer.show();
		bot.sleep(TIME_1S);
		
		// TODO modify web page and check changes
		// projectExplorer.openFile(projectName, path)

		bot.waitForShell("New HTML File");

		projectExplorer.show();
		projectExplorer.bot().tree().select(0);
		ContextMenuHelper.clickContextMenu(projectExplorer.bot().tree(),
				"Team", "Commit and Push...");

		// if the auth shell appears click on OK
		if (bot.waitForShell("Identify Yourself") != null) {
			bot.button("OK").click();
		}

		bot.waitForShell("Commit Changes");
		bot.styledText(0).setText("comment");

		// select all items to commit
		for (int i = 0; i < bot.table().rowCount(); i++) {
			bot.table().getTableItem(i).toggleCheck();
		}

		bot.button("Commit").click();

		bot.waitForShell("Push to Another Repository");
		bot.button(IDELabel.Button.FINISH).click();

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_UNLIMITED, TIME_1S);


	}

}
