package org.jboss.tools.runtime.reddeer.wizard;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.runtime.reddeer.condition.RuntimeIsDownloading;

public class DownloadRuntimesTaskWizard extends WizardDialog{

	protected final static Logger log = Logger.getLogger(DownloadRuntimesTaskWizard.class);
	
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
		new WaitUntil(new JobIsRunning(), TimePeriod.LONG, false);
		new WaitWhile(new RuntimeIsDownloading(), TimePeriod.getCustom(1200));
		new DefaultShell("New Project Example");
	}

}
