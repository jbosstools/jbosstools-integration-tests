package org.jboss.tools.maven.reddeer.wizards;

import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
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
		new WaitWhile(new ShellWithTextIsActive("Import Maven Projects"),
				TimePeriod.NORMAL);
		try {
			new DefaultShell("Found cheatsheet");
			new PushButton("No").click();
		} catch (Exception ex) {
			// project was without cheatsheet; continue.
		}
		new WaitWhile(new JobIsRunning(), TimePeriod.VERY_LONG);
	}

}
