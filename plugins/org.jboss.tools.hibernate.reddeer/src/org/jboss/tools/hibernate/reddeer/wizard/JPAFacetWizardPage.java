package org.jboss.tools.hibernate.reddeer.wizard;

import org.jboss.reddeer.jface.wizard.WizardPage;
import org.jboss.reddeer.core.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.WidgetIsEnabled;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;
import org.jboss.reddeer.common.wait.AbstractWait;
import org.jboss.reddeer.common.wait.TimePeriod;
import org.jboss.reddeer.common.wait.WaitUntil;
import org.jboss.reddeer.common.wait.WaitWhile;

/**
 * JPA Facets wizard page reddeer implementation
 * @author jpeterka
 *
 */
public class JPAFacetWizardPage extends WizardPage {
	
	/**
	 * Sets JPA platform
	 * @param platform given platform
	 */
	public void setPlatform(JpaPlatform platform) {
		new DefaultCombo(0).setSelection(platform.toString());
	}	

	/**
	 * sets JPA implementation 
	 * @param impl given implementation
	 */
	public void setJpaImplementation(JpaImplementation impl) {
		new DefaultCombo(1).setSelection(impl.toString());
	}

	/**
	 * Sets connection profile for JPA 
	 * @param profileName given connection profile
	 */
	public void setConnectionProfile(String profileName) {
		DefaultGroup group = new DefaultGroup("Connection");
		new WaitUntil(new WidgetIsEnabled(new DefaultCombo(group)));
		new DefaultCombo(group).setSelection(profileName);
		PushButton apply = new PushButton("Apply");
		apply.click();
		new WaitWhile(new JobIsRunning());
		// Abstract wait workaround, various shells are messing with RedDeer
		AbstractWait.sleep(TimePeriod.NORMAL);
	}

	/**
	 * Set JPA autodiscovery
	 * @param autoDiscovery if set to true autodiscovery is set
	 */
	public void setAutoDiscovery(boolean autoDiscovery) {
		if (autoDiscovery) {
			new RadioButton("Discover annotated classes automatically").click();
		}
		
	}

}
