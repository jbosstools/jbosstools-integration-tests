package org.jboss.tools.jsf.reddeer.ui;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;

public class JSFNewProjectSecondPage extends WizardPage{
	
	public void setRuntime(String runtime){
		new DefaultCombo(1).setSelection(runtime);
	}

}
