package org.jboss.tools.maven.ui.bot.test.dialog.maven;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;

public class MavenExampleDialog extends WizardDialog{
	
	public MavenExampleDialog(){
		super();
		addWizardPage(new MavenExampleFirstPage(), 1);
		addWizardPage(new MavenExampleSecondPage(), 2);
		addWizardPage(new MavenExampleThirdPage(), 3);
	}
	
	public void open(){
		new ShellMenu("File","New","Example...").select();
		new DefaultShell("New Example");
		new DefaultTreeItem("JBoss Tools","Project Examples").select();
		new PushButton("Next >").click();
		new DefaultShell("Progress Information");
		new DefaultShell("New Project Example");
	}
	
	public void chooseProject(String... path){
		new DefaultTreeItem(path).select();
	}

}
