package org.jboss.tools.maven.ui.bot.test.dialog.jsf;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;

public class JSFProjectSecondPage extends WizardPage{
	
	public void setRuntime(String runtime){
		new DefaultCombo("Runtime:*").setSelection(runtime);
	}

}
