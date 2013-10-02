package org.jboss.tools.teiid.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.ImportWizardDialog;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.teiid.reddeer.condition.IsInProgress;

/**
 * Wizard for importing relational model from WSDL
 * 
 * @author apodhrad
 * 
 */
public class WsdlWebImportWizard extends ImportWizardDialog {

	public WsdlWebImportWizard() {
		super("Teiid Designer", "WSDL File or URL >> Web Service Model");
	}

	public void importWsdl(String name, String project, String wsdl) {
		open();

		new SWTWorkbenchBot().textWithLabel("Web Service Model Name").setText(name);
		new SWTWorkbenchBot().textInGroup("Target Workspace Folder").setText(project);
		new SWTWorkbenchBot().button("Workspace...").click();

		new DefaultShell("WSDL File Selection");
		new DefaultTreeItem(project, wsdl).select();
		new PushButton("OK").click();

		next();
		next();
		next();
		next();

		finish();
	}

	@Override
	public void finish() {
		super.finish();

		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

}
