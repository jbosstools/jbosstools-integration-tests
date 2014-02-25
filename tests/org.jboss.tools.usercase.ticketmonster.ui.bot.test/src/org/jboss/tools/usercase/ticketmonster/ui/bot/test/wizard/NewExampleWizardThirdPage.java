package org.jboss.tools.usercase.ticketmonster.ui.bot.test.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.table.DefaultTable;

public class NewExampleWizardThirdPage extends WizardPage{
	
	public String getGroupID(){
		return new DefaultCombo("Group Id:").getText();
	}
	
	public String getArtifactID(){
		return new DefaultCombo("Artifact Id:").getText();
	}
	
	public String getVersion(){
		return new DefaultCombo("Version:").getText();
	}
	
	public String getPackage(){
		return new DefaultCombo("Package:").getText();
	}
	
	public Table getTableSuffix(){
		return new DefaultTable();
	}
	

}
