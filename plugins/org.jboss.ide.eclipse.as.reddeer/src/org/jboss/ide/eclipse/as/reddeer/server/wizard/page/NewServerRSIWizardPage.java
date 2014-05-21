package org.jboss.ide.eclipse.as.reddeer.server.wizard.page;

import java.util.LinkedList;
import java.util.List;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.DefaultText;

/**
 * TODO comment here
 * @author Pavol Srna
 *
 */
public class NewServerRSIWizardPage extends WizardPage{
	
	
	/**
	 * Set Remote Server Home path
	 * @param path
	 */
	public void setRemoteServerHome(String path){
		new DefaultText(0).setText(path);
	}

	/**
	 * @return Remote Server Home text
	 */
	public String getRemoteServerHome() {
		return new DefaultText(0).getText();
	}
	
	/**
	 * @return list of all configured hosts
	 */
	public List<String> getHosts() {
		return new LinkedList<String>(getHostCombo().getItems());
	}	
	
	/**
	 * Select host from combo on Remote System Integration wizard page.
	 * @param host to be selected
	 * @return false if host to be selected is not listed in combo
	 */
	public boolean selectHost(String host){
		if(getHosts().contains(host)){
			getHostCombo().setSelection(host);
			return true;
		}
		return false;
	}
	
	private Combo getHostCombo() {
		return new DefaultCombo(0);
	}
	
	public Button getNewHostBtn(){
		return new PushButton("New Host...");
	}
}
