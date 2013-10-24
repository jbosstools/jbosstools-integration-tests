package org.jboss.tools.maven.ui.bot.test.dialog.maven;

import org.jboss.reddeer.eclipse.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;

public class MavenRemoteRepositoriesPreferencePage extends PreferencePage{
	
	public MavenRemoteRepositoriesPreferencePage(){
		super("JBoss Tools","Remote Repositories");
	}
	
	public void addRepository(Repository repository){
		new PushButton("Add...").click();
		new DefaultShell("New Repository");
		new LabeledText("Name:").setText(repository.getName());
		new LabeledText("URL:").setText(repository.getUrl());
		new CheckBox("Enabled").toggle(repository.isEnabled());
		new PushButton("OK").click();
		new DefaultShell("Preferences");
	}
	
	public void modifyRepository(Repository oldRepo, Repository newRepo){
		new DefaultTable().select(oldRepo.getName());
		new PushButton("Edit...").click();
		new DefaultShell("Edit Repository");
		new LabeledText("Name:").setText(newRepo.getName());
		new LabeledText("URL:").setText(newRepo.getUrl());
		new CheckBox("Enabled").toggle(newRepo.isEnabled());
		new PushButton("OK").click();
		new DefaultShell("Preferences");
	}
	
	public void deleteRepository(Repository repository){
		new DefaultTable().select(repository.getName());
		new PushButton("Remove").click();
	}
	
	public Repository getRepository(String name){
		try{
			new DefaultTable().select(name);
		}	catch(SWTLayerException ex){
			return null;
		}
		new PushButton("Edit...").click();
		new DefaultShell("Edit Repository");
		String repoName = new LabeledText("Name:").getText();
		String URL = new LabeledText("URL:").getText();
		boolean enabled = new CheckBox("Enabled").isChecked();
		new PushButton("OK").click();
		new DefaultShell("Preferences");
		Repository r = new Repository(repoName,URL,enabled);
		return r;
	}
}