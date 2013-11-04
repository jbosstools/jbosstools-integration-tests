package org.jboss.ide.eclipse.as.reddeer.server.wizard.page;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.eclipse.wst.server.ui.wizard.NewServerWizardPage;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;

/**
 * 
 * @author psrna
 *
 */
public class DefineNewServerWizardPage extends NewServerWizardPage {
	
	public DefineNewServerWizardPage(WizardDialog parentDialog) {
		super(parentDialog);
	}

	/**
	 * Selects server of specified type.
	 * @param type represents server node label as seen in server tree.
	 * For example "JBoss AS 7.1"
	 */
	public void selectType(String type){
		
		DefaultTree t = new DefaultTree();
		for(TreeItem i : t.getAllItems()){
			if(i.getText().equals(type)){
				i.select();
				break;
			}
		}
	}
	
}
