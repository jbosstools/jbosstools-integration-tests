package org.jboss.tools.maven.reddeer.wizards;

import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;


public class ConfigureMavenRepositoriesWizard{
	
	public void open(){
		new PushButton("Configure Maven Repositories...").click();
		new DefaultShell("Maven Repositories");
	}
	
	public String chooseRepositoryFromList(String repo, boolean activeByDefault){
		new PushButton(" Add Repository...").click();
		new DefaultShell("Add Maven Repository");
		new DefaultCombo(0).setSelection(repo);
		new CheckBox("Active by default").toggle(activeByDefault);
		String a =new LabeledText("Name:").getText();
		String b = new LabeledText("URL:").getText();
		String nameWithUrl = a+"-"+b;
		new PushButton("OK").click();
		new DefaultShell("Maven Repositories");
		return nameWithUrl;
	}
	
	public String addRepository(String repoID, String repoURL, boolean activeByDefault){
		new PushButton(" Add Repository...").click();
		new DefaultShell("Add Maven Repository");
		new DefaultCombo(0).setText(repoID);
		new CheckBox("Active by default").toggle(activeByDefault);
		new LabeledText("ID:").setText(repoID);
		new LabeledText("Name:").setText(repoID);
		String a =new LabeledText("Name:").getText();
		new LabeledText("URL:").setText(repoURL);
		String b = new LabeledText("URL:").getText();
		String nameWithUrl = a+"-"+b;
		new PushButton("OK").click();
		new DefaultShell("Maven Repositories");
		return nameWithUrl;
	}
	
	public void removeRepo(String repo) {
		new DefaultTable(new DefaultGroup("Repositories")).select(repo);
		new PushButton(" Remove ").click();
		new DefaultShell("Question?");
		new PushButton("Yes").click();
		new DefaultShell("Maven Repositories");
		
	}
	
	public boolean removeAllRepos() {
		if(new DefaultTable(new DefaultGroup("Repositories")).rowCount() > 0){
			new PushButton(" Remove All ").click();
			new DefaultShell("Question?");
			new PushButton("Yes").click();
			new DefaultShell("Maven Repositories");
			return true;
		}
		return false;
		
	}
	
	public void editRepo(String repo,boolean activeByDefault,String id, String name,String url){
		new DefaultTable(new DefaultGroup("Repositories")).select(repo);
		new PushButton(" Edit Repository...").click();
		new DefaultShell("Edit Maven Repository");
		new CheckBox("Active by default").toggle(activeByDefault);
		if(id != null){
			new LabeledText("ID:").setText(id);
		}
		if(name != null){
			new LabeledText("Name:").setText(name);
		}
		if(url!=null){
			new LabeledText("URL:").setText(url);
		}
		new PushButton("OK").click();
		new DefaultShell("Maven Repositories");
	}
	
	public void confirm(){
		new DefaultShell("Maven Repositories");
		new PushButton("Finish").click();
		try{
			new WaitUntil(new ShellWithTextIsActive("Confirm File Update"),TimePeriod.NORMAL);
			new DefaultShell("Confirm File Update");
			new PushButton("Yes").click();
		} catch(WaitTimeoutExpiredException ex){
			
		}
	}
	
	public void cancel(){
		new DefaultShell("Maven Repositories");
		new PushButton("Cancel").click();
	}

}
