package org.jboss.tools.maven.reddeer.wizards;

import java.util.List;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.api.Tree;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.LabeledCombo;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;

public class MavenImportWizardFirstPage extends WizardPage{
	
	public void setRootDirectory(String path){
		new LabeledCombo("Root Directory:").setText(path);
		new PushButton("Refresh").click();
		new WaitUntil(new ProjectIsLoaded(new DefaultTree()), TimePeriod.LONG);
	}
	
	public void importProject(String path){
		setRootDirectory(path);
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsActive("Import Maven Projects"),TimePeriod.NORMAL);
		try{
			new DefaultShell("Found cheatsheet");
			new PushButton("No").click();
		}catch(Exception ex){
			//project was without cheatsheet; continue.
		}
		new WaitWhile(new JobIsRunning(),TimePeriod.VERY_LONG);
	}
	
	public List<TreeItem> getProjects(){
		DefaultTree tree = new DefaultTree();
		return tree.getAllItems();
	}
	
	
	private class ProjectIsLoaded implements WaitCondition {

		private Tree tree;
		
		private ProjectIsLoaded(Tree tree) {
			this.tree = tree;
		}
		
		@Override
		public boolean test() {
			return !tree.getItems().isEmpty();
		}

		@Override
		public String description() {
			return "At least one project is loaded";
		}
	}

}
