package org.jboss.tools.modeshape.rest.ui.bot.ext.dialog;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.jboss.tools.ui.bot.ext.wizards.SWTBotWizard;

/**
 * 
 * This class represents dialog for setting the modeshape server.
 * 
 * @author apodhrad
 * 
 */
public class ModeshapeServerDialog extends SWTBotWizard {

	public static final String LABEL_URL = "URL:";
	public static final String LABEL_USER = "User:";
	public static final String LABEL_PASSWORD = "Password:";

	public ModeshapeServerDialog(SWTBotShell dialog) {
		super(dialog.activate().widget);
	}

	public String getUrl() {
		return getTextField(LABEL_URL).getText();
	}

	public void setUrl(String url) {
		getTextField(LABEL_URL).setText(url);
	}

	public String getUser() {
		return getTextField(LABEL_USER).getText();
	}

	public void setUser(String user) {
		getTextField(LABEL_USER).setText(user);
	}

	public String getPassword() {
		return getTextField(LABEL_PASSWORD).getText();
	}

	public void setPassword(String password) {
		getTextField(LABEL_PASSWORD).setText(password);
	}

	public boolean testServerConnection() {
		clickButton("Test");

		SWTBotShell shell = bot().shell("Test Server Connection");
		shell.activate();
		String message = shell.bot().label(1).getText();
		shell.bot().button("OK").click();

		activate();
		return message.equals("Successfully connected using the specified server properties.");
	}

	private SWTBotText getTextField(String label) {
		return bot().textWithLabel(label);
	}

}
