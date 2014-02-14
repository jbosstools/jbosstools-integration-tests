package org.jboss.ide.eclipse.as.reddeer.server.wizard.page;

import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardPage;
import org.jboss.reddeer.swt.api.Combo;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;

/**
 * First wizard page of wizard "New Server"
 * 
 * File > New > Other... > Server > Server
 * 
 * @author psrna
 * @author Radoslav Rabara
 */
public class DefineNewServerWizardPage extends NewServerWizardPage {
	
	/**
	 * Selects server of specified type in specified category.
	 * 
	 * @param category represents category of servers as seen in server tree.
	 * @param type represents server node label as seen in server tree.
	 * 
	 * For example "JBoss Community" "JBoss AS 7.1"
	 */
	public void selectType(String category, String type){
		DefaultTree t = new DefaultTree();
		for(TreeItem i : t.getItems()) {
			if(i.getText().equals(category)) {
				for(TreeItem j : i.getItems()) {
					if(j.getText().equals(type)) {
						j.select();
						return;
					}
				}
			}
		}
		throw new IllegalArgumentException("Type " + type + " is not among server types in category "+category+".");
	}

	public String getServerName() {
		return new LabeledText("Server name:").getText();
	}

	public void checkErrors() {
		checkIfThereIsAnotherServerWithSameType();
		
		String text;
		try {
			text = new LabeledText("Define a New Server").getText();
			log.info("Found error text: " + text);
		} catch(SWTLayerException e) {
			log.info("No error text found.");
			return;
		}
		
		checkServerName(text);
	}

	private void checkIfThereIsAnotherServerWithSameType() {
		try {
			//combo box indicates that there are present other servers with the same type
			Combo combo = new DefaultCombo();
			throw new AssertionError("There is another server with the same type.\n"
					+ "Present server: "+combo.getText());
		} catch(SWTLayerException e) {
			//combo box is not present so there is not any other server with the same type
		}
	}

	private void checkServerName(String errorText) {
		if(errorText.contains("The server name is already in use. Specify a different name.")) {
			throw new AssertionError("The server name '"+getServerName()+"' is already in use.");
		}
	}
}
