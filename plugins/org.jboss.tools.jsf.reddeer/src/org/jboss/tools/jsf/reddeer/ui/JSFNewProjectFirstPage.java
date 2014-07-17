package org.jboss.tools.jsf.reddeer.ui;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.DefaultText;

public class JSFNewProjectFirstPage extends WizardPage{
	
	public void setProjectName(String name){
		new DefaultText(0).setText(name);
	}
	
	public void setJSFVersion(String version){
		new DefaultCombo(0).setSelection(version);
	}
	
	public void setJSFType(String type){
		new DefaultCombo(1).setSelection(type);
	}

}
