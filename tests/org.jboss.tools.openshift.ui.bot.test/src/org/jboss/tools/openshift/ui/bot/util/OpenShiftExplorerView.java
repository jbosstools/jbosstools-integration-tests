package org.jboss.tools.openshift.ui.bot.util;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.toolbar.DefaultToolItem;
import org.jboss.reddeer.swt.impl.toolbar.ViewToolItem;
import org.jboss.reddeer.workbench.view.View;

/**
 * 
 * OpenShift explorer view implemented with RedDeer.
 * Currently supports opening view and establishment of connection 
 * 
 * @author mlabuda@redhat.com
 *
 */
public class OpenShiftExplorerView extends View {

	public OpenShiftExplorerView() {
		super("JBoss Tools", "OpenShift Explorer");
	}
	
	public void openConnectionShell() {
		open();
		DefaultToolItem connectionButton = new DefaultToolItem("Connect to OpenShift");
		connectionButton.click();
	}
	
	public void connectToOpenShift(String server, String username, String password, boolean storePassword) {
		new DefaultShell("").setFocus();
		if (new CheckBox(0).isChecked()) {
			new CheckBox(0).click();
		}
		
		new DefaultCombo(1).setText(server);
		new DefaultText(0).setText(username);
		new DefaultText(1).setText(password);
		
		boolean checkedSavePassword = new CheckBox(1).isChecked();
		if ((checkedSavePassword && !storePassword) || 
		    (!checkedSavePassword) && storePassword) {
			new CheckBox(1).click();
		}
		
		new PushButton("Finish").click();
	}	
}
