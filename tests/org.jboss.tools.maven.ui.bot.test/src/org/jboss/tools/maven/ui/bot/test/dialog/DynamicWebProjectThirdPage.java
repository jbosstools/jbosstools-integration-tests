package org.jboss.tools.maven.ui.bot.test.dialog;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.CheckBox;

public class DynamicWebProjectThirdPage extends WizardPage{
	
	public void generateWebXml(boolean generate){
		new CheckBox("Generate web.xml deployment descriptor").toggle(generate);
	}
}

