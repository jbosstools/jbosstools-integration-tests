package org.jboss.tools.maven.reddeer.project.examples.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;

public class ArchetypeExamplesWizardFirstPage extends WizardPage{
	
	public String getProjectName(){
		return new  LabeledCombo("Project name").getText();
	}
	
	public String getPackage(){
		return new  LabeledCombo("Package").getText();
	}
	
	public boolean isUseDefaultWorkspace(){
		return new CheckBox("Use default Workspace location").isChecked();
	}
	
	public String getWorkspaceLocation(){
		return new LabeledCombo("Location:").getText();
	}
	
	public void setProjectName(String projectName){
		new LabeledCombo("Project name").setText(projectName);
	}
	
	public void setPackage(String pckg){
		new  LabeledCombo("Package").setText(pckg);
	}
	
	public void useDefaultWorkspace(boolean useDefault){
		new CheckBox("Use default Workspace location").toggle(useDefault);
	}
	
	public void setWorkspaceLocation(String location){
		new  LabeledCombo("Location:").setText(location);
	}
	
	
}
