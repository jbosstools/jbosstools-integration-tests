package org.jboss.tools.maven.ui.bot.test.dialog.maven;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;

public class MavenExampleThirdPage extends WizardPage{

	public void setGAV(String groupId, String artifactId, String version){
		new DefaultCombo("Group Id:").setText(groupId);
		new DefaultCombo("Artifact Id:").setText(artifactId);
		new DefaultCombo("Version:").setText(version);
	}
	
}
