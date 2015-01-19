package org.jboss.tools.hibernate.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.OkButton;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;

public class JPAFacetWizardPage extends WizardPage {
	
	public void setPlatform(JpaPlatform platform) {
		new DefaultCombo(0).setSelection(platform.toString());
	}	

	public void setJpaImplementation(JpaImplementation impl) {
		new DefaultCombo(1).setSelection(impl.toString());
	}

	public void setConnectionProfile(String profileName) {
		DefaultGroup group = new DefaultGroup("Connection");
		new WaitUntil(new WidgetIsEnabled(new DefaultCombo(group)));
		new DefaultCombo(group).setSelection(profileName);;

		PushButton apply = new PushButton("Apply");
		apply.click();
		new WaitWhile(new JobIsRunning());
		// Abstract wait workaround, various shells are messing with RedDeer
		AbstractWait.sleep(TimePeriod.NORMAL);
	}

}
