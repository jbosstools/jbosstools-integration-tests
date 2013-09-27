package org.jboss.tools.maven.ui.bot.test.dialog.maven;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.condition.ButtonWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;

public class MavenProjectWizardThirdPage extends WizardPage{
	
	public void setGAV(String groupId, String artifactId, String version){
		new WaitUntil(new ButtonWithTextIsActive(new PushButton("Cancel")),TimePeriod.LONG); //wait for progressbar to finish
		new DefaultCombo("Group Id:").setText(groupId);
		new DefaultCombo("Artifact Id:").setText(artifactId);
		if(version!=null){
			new DefaultCombo("Version:").setText(version);
		}
	}

}
