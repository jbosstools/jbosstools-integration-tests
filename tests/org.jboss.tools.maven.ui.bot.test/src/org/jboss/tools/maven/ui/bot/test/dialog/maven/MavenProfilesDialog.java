package org.jboss.tools.maven.ui.bot.test.dialog.maven;

import java.util.ArrayList;

import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ContextMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;

public class MavenProfilesDialog extends WizardDialog{
	
	private String projectName;
	
	public MavenProfilesDialog(String projectName){
		this.projectName=projectName;
	}
	
	public void open(){
		PackageExplorer pe = new PackageExplorer();
		pe.open();
		pe.getProject(projectName).select();
		new ContextMenu("Maven","Select Maven Profiles...").select();
		new DefaultShell("Select Maven profiles");		
	}
	
	public void activateAllProfiles(){
		new PushButton("Select All").click();
	}
	
	public void deselectAllProfiles(){
		new PushButton("Deselect all").click();
	}
	
	public void activateProfile(String profileName){
		try{
			new DefaultTable().check(profileName);
		} catch(IllegalArgumentException ex){
			new DefaultTable().check(profileName+" (auto activated)");
		}
	}
	
	public void deactivateProfile(String profileName){
		try{
			new DefaultTable().select(profileName);
		} catch(IllegalArgumentException ex){
			new DefaultTable().select(profileName+" (auto activated)");
		}
		new PushButton("Deactivate").click();
	}
	
	public java.util.List<String> getAllProfiles(){
		java.util.List<String> profiles = new ArrayList<String>();
		for(int i=0; i<new DefaultTable().rowCount();i++){
			profiles.add(new DefaultTable().cell(i, 0));
		}
		return profiles;
	}
	
	public void ok(){
		new PushButton("OK").click();
		new WaitWhile(new JobIsRunning(),TimePeriod.NORMAL);
	}
	
	public String getActiveProfilesText(){
		return new DefaultText(0).getText();//("Active profiles for "+projectName+":").getText();
	}

}