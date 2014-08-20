package org.jboss.tools.ws.reddeer.ui.preferences;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.workbench.preference.WorkbenchPreferencePage;

/**
 * JBoss Tools > JAX-RS > JAX-RS Validator page in Preferences dialog.
 * 
 * @author Radoslav Rabara
 *
 */
public class JAXRSValidatorPreferencePage extends WorkbenchPreferencePage {

	public JAXRSValidatorPreferencePage() {
		super("JBoss Tools", "JAX-RS", "JAX-RS Validator");
	}

	/**
	 * Enables or disables validation.
	 *
	 * @param enable if it's <code>true</code> then validation will be enabled,
	 * 			otherwise validation
	 */
	public void setEnableValidation(boolean enable) {
		new CheckBox("Enable validation").toggle(enable);
	}
}
