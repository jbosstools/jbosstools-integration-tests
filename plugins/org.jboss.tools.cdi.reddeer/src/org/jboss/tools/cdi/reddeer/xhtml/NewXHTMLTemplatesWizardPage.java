package org.jboss.tools.cdi.reddeer.xhtml;

import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.jface.wizard.WizardPage;
import org.eclipse.reddeer.swt.api.Table;
import org.eclipse.reddeer.swt.api.TableItem;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.table.DefaultTable;

public class NewXHTMLTemplatesWizardPage extends WizardPage {
	
	public NewXHTMLTemplatesWizardPage(ReferencedComposite referencedComposite) {
		super(referencedComposite);
	}

	public void useXHTMLTeplate(boolean useTeplate){
		new CheckBox("Use XHTML Teplate").toggle(useTeplate);
	}
	
	public void selectTeplate(TableItem teplate){
		teplate.select();
	}
	
	public Table getTeplates(){
		return new DefaultTable();
	}

}
