package org.jboss.tools.cdi.reddeer.cdi.ui;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.workbench.preference.WorkbenchPreferencePage;
import org.jboss.tools.cdi.reddeer.CDIConstants;

/**
 * Represents Preference page: 
 * 		JBoss Tools -> CDI (Context and Dependency Injection) -> CDI Validator
 * 
 * @author jjankovi
 *
 */
public class CDIValidatorPreferencePage extends WorkbenchPreferencePage{

	public CDIValidatorPreferencePage() {
		super("JBoss Tools", CDIConstants.CDI_GROUP, "CDI Validator");
	}
	
	public void enableValidation() {
		new CheckBox().toggle(true);
	}
	
	public void disableValidation() {
		new CheckBox().toggle(false);
	}
	
	public boolean isValidationEnabled() {
		return new CheckBox().isChecked();
	}
}
