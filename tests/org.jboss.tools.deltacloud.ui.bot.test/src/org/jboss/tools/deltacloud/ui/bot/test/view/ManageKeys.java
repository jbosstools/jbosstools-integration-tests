package org.jboss.tools.deltacloud.ui.bot.test.view;

import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.jboss.tools.ui.bot.ext.SWTTestExt;
import org.jboss.tools.ui.bot.ext.types.IDELabel;

public class ManageKeys extends SWTBotShell {

	public ManageKeys(Shell shell) throws WidgetNotFoundException {
		super(shell);
	}
	
	public void selectKey(String key) {
		bot().list().select(key);
	}
	
	public void refreshKeys() {
		bot().button("Refresh keys").click();
	}
	public List<String> getKeys() {
		return Arrays.asList(bot().list().getItems());	
	}
	/**
	 * 
	 * @param name
	 * @param savePrivatePart
	 * @return path to .pem file 
	 */
	public String newKey(String name, boolean savePrivatePart) {
		bot().button(IDELabel.Button.NEW).click();
		SWTBot active = bot().activeShell().activate().bot();
		active.text(0).setText(name);
		if (!savePrivatePart) active.checkBox().deselect();
		active.button(IDELabel.Button.OK).click();
		SWTTestExt.util.waitForNonIgnoredJobs();
		return name;
	}
	public void deleteKey(String name) {
		selectKey(name);
		bot().button(IDELabel.Button.DELETE).click();
	}
	public void delteAllKeys() {
		for (String key:getKeys()) {
			deleteKey(key);
		}
	}
	public void clickOK() {
		bot().button(IDELabel.Button.OK).click();
	}
	public void clickCancel() {
		bot().button(IDELabel.Button.CANCEL).click();
	}
}
