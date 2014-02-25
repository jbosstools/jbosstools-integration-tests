package org.jboss.tools.usercase.ticketmonster.ui.bot.test.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.api.Button;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;

public class DownloadRuntimeDialog extends WizardDialog{
	
	private boolean eapDialog;
	
	public DownloadRuntimeDialog(){
		this.eapDialog = false;
	}
		
	public void eapDialog(){
		this.eapDialog = true;
		addWizardPage(new DownloadRuntimeFirstPage(), 0);
		addWizardPage(new DownloadRuntimeSecondPage(), 1);
		addWizardPage(new DownloadEAPRuntimeThirdPage(), 2);
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
		if(!eapDialog){
			Shell activeShell = new DefaultShell("Download 'JBoss AS 7.1.1 (Brontes)");
			new WaitWhile(new ShellWithTextIsActive(activeShell.getText()),TimePeriod.VERY_LONG);
		}
		new DefaultShell("New Project Example");
	}

}
