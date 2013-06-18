package org.jboss.tools.maven.ui.bot.test.dialog.maven;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;

public class MavenExampleSecondPage extends WizardPage {
	
	public void setProjectName(String name){
		new DefaultCombo("Project name").setText(name);
	}
	
	public void setPackage(String pack){
		new DefaultCombo("Package").setText(pack);

	}

}
