package org.jboss.tools.hibernate.reddeer.console;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

public class NewConfigurationLocationPage extends WizardPage {

	
	public void setLocation(String... location) {
		new DefaultTreeItem(location).select();
	}
	
	public void setFilenName(String filename) {
		new DefaultText("File name:").setText(filename);
	}
		
}
