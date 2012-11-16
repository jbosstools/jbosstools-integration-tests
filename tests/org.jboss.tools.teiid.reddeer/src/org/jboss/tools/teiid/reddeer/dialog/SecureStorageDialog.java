package org.jboss.tools.teiid.reddeer.dialog;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Secure Storage Dialog
 * 
 * @author apodhrad
 *
 */
public class SecureStorageDialog extends DefaultShell {

	public static final String TITLE = "Secure Storage";
	public static final String LABEL_PASSWORD = "Password:";

	public SecureStorageDialog() {
		super(TITLE);
	}

	/**
	 * Sets a given password
	 * 
	 * @param password
	 */
	public void setPasword(String password) {
		new LabeledText(LABEL_PASSWORD).setText(password);
	}

	public void ok() {
		new PushButton("OK").click();
	}

}
