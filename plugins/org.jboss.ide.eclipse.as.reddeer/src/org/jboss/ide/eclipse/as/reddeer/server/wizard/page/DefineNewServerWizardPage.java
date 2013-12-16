package org.jboss.ide.eclipse.as.reddeer.server.wizard.page;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardPage;
import org.jboss.reddeer.swt.api.TreeItem;
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
	
	public DefineNewServerWizardPage(WizardDialog parentDialog) {
		super(parentDialog);
	}

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
		throw new AssertionError("Type " + type + " is not among server types in category "+category+".");
	}
}