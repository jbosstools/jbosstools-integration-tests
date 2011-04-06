package org.jboss.tools.deltacloud.ui.bot.test.view;

import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.Timing;
import org.jboss.tools.ui.bot.ext.gen.ActionItem.NewObject;
import org.jboss.tools.ui.bot.ext.types.IDELabel;
import org.jboss.tools.ui.bot.ext.wizards.SWTBotWizard;

public class ConnectionWizard extends SWTBotWizard {

	public static ConnectionWizard create() {
		SWTTestExt.open.newObject(NewObject.create("Deltacloud","Cloud Connection"));
		return new ConnectionWizard();
	}
	public void setName(String name) {
		this.setText("Name:", name);
	}
	public void setURL(String url) {
		this.setText("URL:", url);	
	}
	public void setUsername(String username) {
		this.setText("Username:", username);			
	}
	public void setPassword(String pass) {
		this.setText("Password:", pass);		
	}
	
	public void testConnection() {
		bot().button(IDELabel.Button.TEST).click();
		bot().sleep(Timing.time2S());
		SWTTestExt.util.waitForNonIgnoredJobs();
	}
	public String getErrorWarningText() {
		return bot().text(4).getText();
	}

	protected void setText(String label, String text) {
		super.setText(label, text);
		bot().sleep(Timing.time2S());
	}
}
