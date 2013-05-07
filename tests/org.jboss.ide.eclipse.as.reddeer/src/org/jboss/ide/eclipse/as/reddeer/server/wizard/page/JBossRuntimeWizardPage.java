package org.jboss.ide.eclipse.as.reddeer.server.wizard.page;

import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author psrna
 *
 */
public class JBossRuntimeWizardPage {
	
	public void setRuntimeName(String name){
		new LabeledText("Name").setText(name);
	}

	public void setRuntimeDir(String path){
		new LabeledText("Home Directory").setText(path);
	}
	
}
