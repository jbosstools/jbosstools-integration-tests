package org.jboss.tools.maven.reddeer.wizards;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.api.Tree;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.exception.RedDeerException;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;

public class MavenImportWizard extends WizardDialog {

	public MavenImportWizard() {
		super();
		addWizardPage(new MavenImportWizardFirstPage(), 0);

	}

	public void open() {
		new ShellMenu("File", "Import...").select();
		new WaitUntil(new ShellWithTextIsActive("Import"), TimePeriod.NORMAL);
		new DefaultTreeItem("Maven", "Existing Maven Projects").select();
		new PushButton("Next >").click();
		new WaitUntil(new ShellWithTextIsActive("Import Maven Projects"),
				TimePeriod.NORMAL);
	}

	@Override
	public void finish() {
		new PushButton("Finish").click();
		
		waitForShellToDisappear();
		
		waitForcheatSheet();
		
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}

	private void waitForcheatSheet() {
		try {
			new DefaultShell("Found cheatsheet");
			new PushButton("No").click();
		} catch (Exception ex) {
			// project was without cheatsheet; continue.
		}
	}

	private void waitForShellToDisappear(){
			new WaitWhile(new ShellWithTextIsActive("Import Maven Projects"),
				TimePeriod.NORMAL);
		try{
			//try to wait for another shell (with errors after import)
			new WaitUntil(new ShellWithTextIsActive("Import Maven Projects"));
			throw new MavenImportWizardException(new DefaultShell());
		}catch(WaitTimeoutExpiredException ex){
			//everything is fine
		}
	}

	public class MavenImportWizardException extends RedDeerException{
	
		private static final long serialVersionUID = 1L;
		
		List<String> errors;
		
		public MavenImportWizardException(Shell activeShell) {
			//Probalby some error occured while importing
			super("Probalby some error occured while importing");
			errors = extractErrors(activeShell);
			activeShell.close();
		}
		
		public List<String> getErrors() {
			return errors;
		}

		private List<String> extractErrors(Shell activeShell) {
			List<String> list = new ArrayList<String>();
			Tree tree = new DefaultTree(); 
			for (TreeItem item : tree.getAllItems()) {
				list.add(item.getText());
			}
			return list;
		}
		
	}
	
}
