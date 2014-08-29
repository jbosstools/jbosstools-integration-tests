package org.jboss.tools.cdi.reddeer.xhtml;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.api.TableItem;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.table.DefaultTable;

public class NewXHTMLTemplatesWizardPage extends WizardPage {
	
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
