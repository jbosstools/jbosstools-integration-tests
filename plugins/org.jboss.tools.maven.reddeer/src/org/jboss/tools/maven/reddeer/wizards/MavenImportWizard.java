package org.jboss.tools.maven.reddeer.wizards;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;

public class MavenImportWizard extends WizardDialog{
	
	public MavenImportWizard(){
		super();
		addWizardPage(new MavenImportWizardFirstPage(), 1);
		
	}
	
	public void open(){
		new ShellMenu("File","Import...").select();
		new WaitUntil(new ShellWithTextIsActive("Import"),TimePeriod.NORMAL);
		new DefaultTreeItem("Maven","Existing Maven Projects").select();
		new PushButton("Next >").click();
		new WaitUntil(new ShellWithTextIsActive("Import Maven Projects"),TimePeriod.NORMAL);
	}

}
