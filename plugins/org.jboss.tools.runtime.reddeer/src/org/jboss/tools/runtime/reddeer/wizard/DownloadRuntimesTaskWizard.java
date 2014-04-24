package org.jboss.tools.runtime.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.runtime.reddeer.condition.RuntimeIsDownloaded;

public class DownloadRuntimesTaskWizard extends WizardDialog{

	public void eapDialog(){
		addWizardPage(new TaskWizardFirstPage(), 0);
		addWizardPage(new TaskWizardLoginPage(), 1);
		addWizardPage(new TaskWizardSecondPage(), 2);
		addWizardPage(new TaskWizardThirdPage(), 3);
	}
	
	public void asDialog(){
		addWizardPage(new TaskWizardFirstPage(), 0);
		addWizardPage(new TaskWizardSecondPage(), 1);
		addWizardPage(new TaskWizardThirdPage(), 2);
	}
	
	@Override
	public void finish() {
		log.info("Finish wizard");
		new DefaultShell();
		Button button = new PushButton("Finish");
		button.click();
		new WaitUntil(new JobIsRunning(), TimePeriod.NORMAL, false);
		new WaitUntil(new RuntimeIsDownloaded(), TimePeriod.VERY_LONG);
		new DefaultShell("New Project Example");
	}

}
