package org.jboss.tools.maven.ui.bot.test.dialog;

import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.condition.WaitCondition;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.tree.ShellTree;
import org.jboss.reddeer.swt.impl.tree.ShellTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;

public class ImportMavenProjectWizard {

	public void open(){
		new ShellMenu("File","Import...").select();
		new WaitUntil(new ShellWithTextIsActive("Import"),TimePeriod.NORMAL);
		new ShellTreeItem("Maven","Existing Maven Projects").select();
		new PushButton("Next >").click();
		new WaitUntil(new ShellWithTextIsActive("Import Maven Projects"),TimePeriod.NORMAL);
	}
	
	public void importProject(String path){
		new DefaultCombo("Root Directory:").setText(path);
		new PushButton("Refresh").click();
		new WaitUntil(new ProjectIsLoaded(new ShellTree()), TimePeriod.LONG);
		new PushButton("Finish").click();
	}
	
	private class ProjectIsLoaded implements WaitCondition {

		private ShellTree tree;
		
		private ProjectIsLoaded(ShellTree tree) {
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
