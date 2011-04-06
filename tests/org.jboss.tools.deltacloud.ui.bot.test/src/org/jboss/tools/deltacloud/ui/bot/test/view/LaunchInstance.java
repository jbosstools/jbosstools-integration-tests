package org.jboss.tools.deltacloud.ui.bot.test.view;

import org.jboss.tools.deltacloud.ui.bot.test.Util;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.wizards.SWTBotWizard;

public class LaunchInstance extends SWTBotWizard {

	public void setName(String name) {
		this.setText("Name:", name);
	}
	public void setImage(String image) {
		this.setText("Image:", Util.getImageId(image));	
	}
	public void setKey(String key) {
		this.setText("Key Name:", key);			
	}
	public void setCreateRSE(boolean create) {
		 if (create) bot().checkBox("Create RSE Connection").select();
		 bot().checkBox("Create RSE Connection").deselect();			
	}
	public void setCreateServer(boolean create) {
		if (create) bot().checkBox("Create Server Adapter").select();
		bot().checkBox("Create Server Adapter").deselect();
	}
	public ManageKeys manageKeys() {
		bot().button("Manage...").click();		
		return new ManageKeys(SWTTestExt.bot.shell("Manage Keys").widget);
	}
}
