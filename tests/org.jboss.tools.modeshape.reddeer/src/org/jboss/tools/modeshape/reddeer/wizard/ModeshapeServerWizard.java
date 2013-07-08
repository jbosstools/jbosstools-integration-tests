package org.jboss.tools.modeshape.reddeer.wizard;

import static org.junit.Assert.assertEquals;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.util.Bot;

/**
 * Wizard for creating new ModeShape server.
 * 
 * @author apodhrad
 * 
 */
public class ModeshapeServerWizard extends WizardDialog {

	public static final String DIALOG_TITLE = "New Server";

	public static final String LABEL_URL = "URL:";
	public static final String LABEL_USER = "User:";
	public static final String LABEL_PASSWORD = "Password:";

	public ModeshapeServerWizard activate() {
		Bot.get().shell(DIALOG_TITLE).activate();
		return this;
	}

	public ModeshapeServerWizard setUrl(String url) {
		new LabeledText(LABEL_URL).setText(url);
		return this;
	}

	public ModeshapeServerWizard setUser(String user) {
		new LabeledText(LABEL_USER).setText(user);
		return this;
	}

	public ModeshapeServerWizard setPassword(String password) {
		new LabeledText(LABEL_PASSWORD).setText(password);
		return this;
	}

	public ModeshapeServerWizard testServerConnection() {
		new PushButton("Test").click();
		Bot.get().shell("Test Server Connection").activate();
		String message = Bot.get().label(1).getText();
		assertEquals(message, "Successfully connected using the specified server properties.", message);
		new PushButton("OK").click();

		return this;
	}
}
