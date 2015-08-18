package org.jboss.tools.forge.reddeer.ui.wizard;

import org.jboss.reddeer.common.condition.WaitCondition;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Forge Connection Profile wizard page Reddeer implementation
 * 
 * @author jrichter
 *
 */
public class ConnectionProfileWizardPage extends WizardPage {

	/**
	 * Sets the connection name field to a given value
	 * 
	 * @param name
	 */
	public void setConnectionName(String name) {
		new LabeledText("Connection Name:").setText(name);
	}

	/**
	 * Sets the JDBC URL field to a given value
	 * 
	 * @param url
	 */
	public void setJdbcUrl(String url) {
		new LabeledText("JDBC URL:").setText(url);
	}

	/**
	 * Sets the User Name field to a given value
	 * 
	 * @param name
	 */
	public void setUserName(String name) {
		new LabeledText("User Name:").setText(name);
	}

	/**
	 * Sets the Driver Location field to a given value
	 * 
	 * @param path
	 *            Path to the driver implementation
	 */
	public void setDriverLocation(String path) {
		new LabeledText("Driver Location:").setText(path);
	}

	/**
	 * Selects the given hibernate dialect
	 * 
	 * @param dialect
	 */
	public void setHibernateDialect(String dialect) {
		new DefaultCombo(0).setSelection(dialect);
	}

	/**
	 * Sets whether or not to verify the connection, waits for confirmation if
	 * set to verify
	 * 
	 * @param shouldVerify
	 *            true to verify connection, false otherwise
	 */
	public void toggleVerifyConnection(boolean shouldVerify) {
		new CheckBox("Verify Database Connection").toggle(shouldVerify);
		if (shouldVerify) {
			new WaitUntil(new ConnectionIsVerified());
		}
	}

	private class ConnectionIsVerified implements WaitCondition {

		private final String successString = "Connection successful.";

		@Override
		public boolean test() {
			return new DefaultText(5).getText().contains(successString);
		}

		@Override
		public String description() {
			return "Required text is " + successString + ", current text is " + new DefaultText(5).getText();
		}

	}
}
