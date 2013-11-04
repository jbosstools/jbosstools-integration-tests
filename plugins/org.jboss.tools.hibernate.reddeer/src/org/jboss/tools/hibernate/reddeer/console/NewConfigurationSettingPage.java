package org.jboss.tools.hibernate.reddeer.console;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * New Hibernate Configuration Wizard page
 * @author Jiri Peterka
 *
 */
public class NewConfigurationSettingPage extends WizardPage {
	
	public void setDatabaseDialect(String dialect) {
		new DefaultCombo("Database dialect:").setSelection(dialect);
	}
	
	public void setDriverClass(String driverClass) {
		new DefaultCombo("Driver class:").setSelection(driverClass);
	}
	
	public void setConnectionURL(String url) {
		new DefaultCombo("Connection URL:").setText(url);
	}
	
	public void setUsername(String username) {
		new LabeledText("Username:").setText(username);
	}
	public void setPassword(String username) {
		new LabeledText("Password:").setText(username);
	}	
	public void setCreateConsoleConfiguration(boolean create) {
		CheckBox cb = new CheckBox();
		boolean status = cb.isChecked();
		if (status != create) {
			cb.click();
		}
	}	
}
