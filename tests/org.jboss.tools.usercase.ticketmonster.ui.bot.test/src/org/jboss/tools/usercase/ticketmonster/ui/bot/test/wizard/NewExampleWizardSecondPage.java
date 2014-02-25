package org.jboss.tools.usercase.ticketmonster.ui.bot.test.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;

public class NewExampleWizardSecondPage extends WizardPage{
	
	public String getProjectName(){
		return new DefaultCombo("Project name").getText();
	}
	
	public String getPackage(){
		return new DefaultCombo("Package").getText();
	}
	
	public boolean isUseDefaultWorkspace(){
		return new CheckBox("Use default Workspace location").isChecked();
	}
	
	public String setWorkspaceLocation(){
		return new DefaultCombo("Location:").getText();
	}
	
	public void setProjectName(String projectName){
		new DefaultCombo("Project name").setText(projectName);
	}
	
	public void setPackage(String pckg){
		new DefaultCombo("Package").setText(pckg);
	}
	
	public void useDefaultWorkspace(boolean useDefault){
		new CheckBox("Use default Workspace location").toggle(useDefault);
	}
	
	public void setWorkspaceLocation(String location){
		new DefaultCombo("Location:").setText(location);
	}
	
	
}
