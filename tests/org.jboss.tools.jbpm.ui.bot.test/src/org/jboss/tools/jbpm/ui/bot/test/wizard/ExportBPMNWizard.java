package org.jboss.tools.jbpm.ui.bot.test.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.ExportWizardDialog;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

/**
 * 
 * @author apodhrad
 *
 */
public class ExportBPMNWizard extends ExportWizardDialog {

	public ExportBPMNWizard() {
		super("BPMN", "BPMN to jPDL");
	}

	public void exportFile(String... path) {
		open();
		
		next();
		next();
		next();
		
		new DefaultTreeItem(path).select();
		
		finish();
	}
}
