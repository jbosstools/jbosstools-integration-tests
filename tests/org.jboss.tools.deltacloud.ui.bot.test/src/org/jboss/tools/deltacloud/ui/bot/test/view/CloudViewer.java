package org.jboss.tools.deltacloud.ui.bot.test.view;

import static org.eclipse.swtbot.swt.finder.waits.Conditions.shellIsActive;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.TimeoutException;
import org.jboss.tools.deltacloud.ui.bot.test.Util;
import org.jboss.tools.ui.bot.ext.Assertions;
import org.jboss.tools.ui.bot.ext.SWTBotExt;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.View;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.view.ViewBase;

public class CloudViewer extends ViewBase {
	Logger log = Logger.getLogger(CloudViewer.class);
	SWTBotExt bot = SWTTestExt.bot;
	public CloudViewer() {
		this.viewObject = View.create("Deltacloud","Cloud Viewer");
	}
	public CloudConnection getConnection(String name) {
		this.show().bot().tree().getTreeItem(name);
		return new CloudConnection(this,name);
	}
	public boolean existsConnection(String name) {
		try {
			this.show().bot().tree().getTreeItem(name);
			return true;
		}
		catch (WidgetNotFoundException e) {
			return false;
		}
	}
	public void addConnection(String name, String url, String user, String password) {
		if (existsConnection(name)) {
			return;
		}
		assertTrue("Deltacloud server is not listening on "+url,Util.isDeltacloudRunning(url));
		assertNotNull("Secure Stoarge password is not configured",SWTTestExt.configuredState.getSecureStoragePassword());
		CloudViewer cloud = new CloudViewer();
		cloud.show().bot().tree().contextMenu("New Connection").click();
		ConnectionWizard shell = new ConnectionWizard();
		Assertions.assertControlEnabled(shell.bot().button(IDELabel.Button.FINISH), false);
		shell.setName(name);
		shell.setURL(url);
		shell.setUsername(user);
		shell.setPassword(password);
		shell.testConnection();
		try {
			bot.waitUntil(shellIsActive("Secure Storage"), Timing.time3S());
			SWTBot st = bot.shell("Secure Storage").activate().bot();
			st.textWithLabel("Password:").setText(SWTTestExt.configuredState.getSecureStoragePassword());
			st.button(IDELabel.Button.OK).click();
		}
		catch (TimeoutException ex) {
			
		}
		util.waitForNonIgnoredJobs();
		Assertions.assertControlEnabled(shell.bot().button(IDELabel.Button.FINISH), true);
		shell.finish();
		
	}

}
