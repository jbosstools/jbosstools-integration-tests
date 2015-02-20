package org.jboss.tools.hibernate.reddeer.console;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * New Hibernate Configuration Wizard page
 * @author Jiri Peterka
 *
 */
public class NewConfigurationSettingPage extends WizardPage {
	
	private final String CONNECTION_URL = "Connection URL:";
	private final String DRIVER_CLASS = "Driver class:";
	private final String USERNAME = "Username:";
	private final String PASSWORD = "Password:";
	
	public void setDatabaseDialect(String dialect) {
		new LabeledCombo("Database dialect:").setText(dialect);
	}
	
	public void setDriverClass(String driverClass) {
		new LabeledCombo(DRIVER_CLASS).setText(driverClass);
	}
	
	public void setConnectionURL(String url) {
		new LabeledCombo(CONNECTION_URL).setText(url);
	}
	
	public void setUsername(String username) {
		new LabeledText(USERNAME).setText(username);
	}
	public void setPassword(String username) {
		new LabeledText(PASSWORD).setText(username);
	}	
	public void setCreateConsoleConfiguration(boolean create) {
		CheckBox cb = new CheckBox();
		boolean status = cb.isChecked();
		if (status != create) {
			cb.click();
		}
	}	
	
	public String getDriveClass() {
		String driveClass = new LabeledCombo(DRIVER_CLASS).getSelection();
		return driveClass;		
	}
	
	public String getConnectionURL() {
		String url  = new LabeledCombo(CONNECTION_URL).getText();
		return url;
	}
	
	public String getUsername() {
		String username = new LabeledText(USERNAME).getText();
		return username;
	}
	
	public String getPassword() {
		String password = new LabeledText(PASSWORD).getText();
		return password;
	}
}
