package org.jboss.tools.hibernate.reddeer.page;

import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.WaitUntil;

public class GenerateEntitiesWizardPage  {

	public GenerateEntitiesWizardPage() {
		new WaitUntil(new ShellWithTextIsActive("Generate Entities"));
	}
	
	public void setUseConsole(boolean useConsole) {
		CheckBox cbUseConsole = new CheckBox("Use Console Configuration");
		if (cbUseConsole.isEnabled() != useConsole) {
			cbUseConsole.click();
		}
	}
	
	public void setPackage(String pkg) {
		new LabeledText("Package:").setText(pkg);
	}
	
	public void setHibernateVersion(String version) {
		new LabeledCombo("Hibernate Version:").setSelection(version);
	}
	
	public void setDatabaseConnection(String profileName) {
		new LabeledCombo("Database Connection").setSelection(profileName);
	}
		
}
