package org.jboss.tools.cdi.reddeer.cdi.ui;

import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.jface.preference.PreferencePage;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.jboss.tools.cdi.reddeer.CDIConstants;

/**
 * Represents Preference page: 
 * 		JBoss Tools -> CDI (Context and Dependency Injection) -> CDI Validator
 * 
 * @author jjankovi
 *
 */
public class CDIValidatorPreferencePage extends PreferencePage{

	public CDIValidatorPreferencePage(ReferencedComposite referencedComposite) {
		super(referencedComposite, "JBoss Tools", CDIConstants.CDI_GROUP, "CDI Validator");
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
