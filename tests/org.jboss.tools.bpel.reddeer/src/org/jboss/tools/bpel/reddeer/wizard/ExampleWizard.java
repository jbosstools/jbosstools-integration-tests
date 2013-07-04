package org.jboss.tools.bpel.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.bpel.reddeer.condition.IsInProgress;

/**
 * 
 * @author apodhrad
 * 
 */
public class ExampleWizard extends NewWizardDialog {

	private String[] examplePath;

	public ExampleWizard(String... examplePath) {
		super("Examples", "JBoss Tools", "Project Examples");
		this.examplePath = examplePath;
	}

	public void execute() {
		open();

		new WaitWhile(new IsInProgress(), TimePeriod.LONG);
		try {
			new DefaultTreeItem(0, examplePath).select();
		} catch (Exception e) {
			throw new RuntimeException("Example '" + arrayToString(examplePath) + "' doesn't exist!");
		}

		finish();

		try {
			new DefaultShell("New Project Example");
			new PushButton("Cancel").click();
		} catch (Exception e) {
			// ok, dialog 'New Project Example' wasn't displayd
		}
	}

	private static String arrayToString(String[] array) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			if (i > 0) {
				result.append(" > ");
			}
			result.append(array[i]);
		}
		return result.toString();
	}
}
