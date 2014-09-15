package org.jboss.tools.maven.reddeer.project.examples.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.api.Table;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.table.DefaultTable;

public class ArchetypeExamplesWizardPage extends WizardPage{
	
	public String getGroupID(){
		return new LabeledCombo("Group Id:").getText();
	}
	
	public void setGroupID(String groupId){
		new LabeledCombo("Group Id:").setText(groupId);
	}
	
	public String getArtifactID(){
		return new LabeledCombo("Artifact Id:").getText();
	}
	
	public String getVersion(){
		return new LabeledCombo("Version:").getText();
	}
	
	public String getPackage(){
		return new LabeledCombo("Package:").getText();
	}
	
	public Table getTableSuffix(){
		return new DefaultTable();
	}
	

}
