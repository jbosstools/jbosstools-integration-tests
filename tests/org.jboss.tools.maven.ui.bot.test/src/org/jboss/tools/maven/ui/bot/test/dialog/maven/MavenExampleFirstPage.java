package org.jboss.tools.maven.ui.bot.test.dialog.maven;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;

public class MavenExampleFirstPage extends WizardPage {
	
	public void setRuntime(String runtime){
		new DefaultCombo("Target Runtime").setSelection(runtime);
	}
	
	public void createBlanProject(boolean toggle){
		new CheckBox("Create a blank project").toggle(toggle);
	}

}
