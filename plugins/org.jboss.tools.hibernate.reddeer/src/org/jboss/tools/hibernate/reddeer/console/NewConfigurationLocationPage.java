package org.jboss.tools.hibernate.reddeer.console;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * Hibernate Console Configuration Location page
 * @author jpeterka
 *
 */public class NewConfigurationLocationPage extends WizardPage {

	/**
	 * Sets location
	 * @param location given location
	 */
	public void setLocation(String... location) {
		new DefaultShell("");
		new DefaultTreeItem(location).select();
	}
	
	/**
	 * Sets location file name
	 * @param filename given file name
	 */
	public void setFilenName(String filename) {
		new DefaultShell("");
		new DefaultText("File name:").setText(filename);
	}
		
}
