package org.jboss.tools.usercase.ticketmonster.ui.bot.test.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.tools.usercase.ticketmonster.ui.bot.test.condition.RuntimeIsDownloaded;

public class DownloadRuntimeDialog extends WizardDialog{

	public void eapDialog(){
		addWizardPage(new DownloadRuntimeFirstPage(), 0);
		addWizardPage(new TaskWizardPage(), 1);
		addWizardPage(new DownloadRuntimeSecondPage(), 2);
		addWizardPage(new DownloadRuntimeThirdPage(), 3);
	}
	
	public void asDialog(){
		addWizardPage(new DownloadRuntimeFirstPage(), 0);
		addWizardPage(new DownloadRuntimeSecondPage(), 1);
		addWizardPage(new DownloadRuntimeThirdPage(), 2);
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
