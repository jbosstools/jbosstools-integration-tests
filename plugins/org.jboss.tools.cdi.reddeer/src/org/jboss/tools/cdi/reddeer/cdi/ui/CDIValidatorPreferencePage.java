/*******************************************************************************
 * Copyright (c) 2010-2018 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
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
