package org.jboss.tools.openshift.ui.bot.test.app;

import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.reddeer.eclipse.ui.browser.BrowserView;
import org.jboss.reddeer.swt.impl.browser.InternalBrowser;
import org.jboss.tools.openshift.ui.bot.test.OpenShiftBotTest;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftUI;
import org.jboss.tools.openshift.ui.bot.util.OpenShiftUI.WebBrowser;
import org.jboss.tools.ui.bot.ext.condition.NonSystemJobRunsCondition;
import org.jboss.tools.ui.bot.ext.helper.ContextMenuHelper;
import org.jboss.tools.ui.bot.ext.parts.SWTBotBrowserExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.junit.After;
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
		SWTBotEditor indexEditor = projectExplorer.openFile(
				DYI_APP + "  [" + DYI_APP + " master]", "diy", "index.html");
		bot.sleep(TIME_5S);
		
		indexEditor.toTextEditor().selectLine(5);
		indexEditor.toTextEditor().insertText(" <title>Welcome to OpSh</title>");
		indexEditor.save();
		
		projectExplorer.show();
		projectExplorer.bot().tree().select(0);
		ContextMenuHelper.clickContextMenu(projectExplorer.bot().tree(),
				"Team", "Commit...");
		
		// if the auth shell appears click on OK
		if (bot.waitForShell("Identify Yourself") != null) {
			bot.button("OK").click();
		}

		bot.waitForShell("Commit Changes");
		bot.styledText(0).setText("comment");

		bot.button("Commit and Push").click();
		bot.sleep(TIME_1S);
		
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_UNLIMITED, TIME_1S);

		bot.button("OK").click();

		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_UNLIMITED, TIME_1S);

		SWTBotView openshiftExplorer = open
				.viewOpen(OpenShiftUI.Explorer.iView);

		SWTBotTreeItem account = openshiftExplorer.bot().tree().getAllItems()[0];
		account.contextMenu("Refresh").click();
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 3, TIME_1S);
		
		account.expand();
		bot.sleep(3000);
				
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 3, TIME_1S);
		
		account.getNode(0).expand();
		bot.sleep(3000);
				
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 3, TIME_1S);
		
		account.getNode(0).getNode(0).select().contextMenu("Show in Web Browser").click();
		
		bot.waitWhile(new NonSystemJobRunsCondition(), TIME_60S * 2, TIME_1S);

		//TODO find a way to verify browsed page, also in DebugFeature
	}

	@After
	public void deleteDIYApp() {
		deleteOpenShiftApplication(DYI_APP, OpenShiftUI.AppType.DIY);
	}
	
}
