package org.jboss.tools.usercase.ticketmonster.ui.bot.test.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.label.DefaultLabel;

public class DownloadEAPRuntimeThirdPage extends WizardPage{
	
	public String getLink(){
		return null;
	}
	
	public boolean containsWarning(){
		return new DefaultLabel(2).getText().contains("This runtime is only available as manual download.");
	}

}
