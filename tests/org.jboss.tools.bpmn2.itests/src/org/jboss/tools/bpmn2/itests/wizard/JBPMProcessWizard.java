package org.jboss.tools.bpmn2.itests.wizard;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class JBPMProcessWizard extends NewWizardDialog {

	/**
	 * Creates a new instance of NewJBpmProcessWizard.
	 */
	public JBPMProcessWizard() {
		super("BPMN2", "jBPM Process Diagram");
	}
	
	@Override
	public WizardPage getFirstPage() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @param fileName
	 */
	public void execute(String fileName) {
		execute(new String[0], fileName);	
	}
	
	/**
	 *
	 * @param location
	 * @param fileName
	 */
	public void execute(String[] location, String fileName) {
		execute(location, fileName, null, null, null);
	}

	/**
	 * 
	 * @param location
	 * @param fileName
	 * @param processName
	 * @param processID
	 * @param pkg
	 */
	public void execute(String[] location, String fileName, String processName, String processId, String packageName) {
		open();
		// if these are null use predefined values by the editor wizard.
		if (processName != null && !processName.isEmpty()) new LabeledText("Process name:").setText(processName);
		if (packageName != null && !packageName.isEmpty()) new LabeledText("Package:").setText(packageName);
		if (processId != null && !processId.isEmpty()) new LabeledText("Process ID:").setText(processId);
		if (location != null && location.length > 0) new LabeledText("Container:").setText(ProjectPath.valueOf(location));
		if (fileName != null && !fileName.isEmpty()) new LabeledText("File name:").setText(fileName);
		finish();
	}
	
	// FIX for: org.jboss.reddeer.swt.exception.SWTLayerException: No shell is available at the moment
	@Override
	public void open() {
		SWTWorkbenchBot bot = new SWTWorkbenchBot();
		bot.menu("File").menu("New").menu("Other...").click();
		SWTBotShell shell = bot.shell("New");
		shell.activate();
		SWTBotTree tree = bot.tree();
		tree.expandNode("BPMN2", "jBPM Process Diagram").select();
		SWTBotButton button = bot.button("Next >");
		button.click();
	}
}
