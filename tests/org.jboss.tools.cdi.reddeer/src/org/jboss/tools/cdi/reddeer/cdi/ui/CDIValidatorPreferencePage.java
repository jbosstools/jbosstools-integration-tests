package org.jboss.tools.cdi.reddeer.cdi.ui;

import org.jboss.reddeer.eclipse.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.impl.button.CheckBox;

/**
 * Represents Preference page: 
 * 		JBoss Tools -> CDI (Context and Dependency Injection) -> CDI Validator
 * 
 * @author jjankovi
 *
 */
public class CDIValidatorPreferencePage extends PreferencePage {

	public CDIValidatorPreferencePage() {
		super("JBoss Tools", "CDI (Context and Dependency Injection)", "CDI Validator");
	}
	
	public void enableValidation() {
		new CheckBox().toggle(true);
	}
	
	public void disableValidation() {
		new CheckBox().toggle(false);
	}
	
}
