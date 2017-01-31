/******************************************************************************* 
 * Copyright (c) 2016 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.cdi.reddeer.cdi.ui.preferences;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.ui.dialogs.PropertyPage;
import org.jboss.reddeer.swt.impl.button.LabeledCheckBox;

/**
 * "CDI (Contexts and Dependency Injection) Settings" property page. 
 * 
 * @author Lukas Valach
 *
 */
public class CDISettingsPreferencePage extends PropertyPage {
	
	public static final String NAME = "CDI (Contexts and Dependency Injection) Settings"; 

	private static final Logger log = Logger.getLogger(CDISettingsPreferencePage.class);
	
	/**
	 * Constructs a new CDI property page.
	 */
	public CDISettingsPreferencePage() {
		super(NAME);
	}
	
	/**
	 * Sets 'CDI support' checkbox to state 'checked'.
	 *
	 * @param checked whether checked or not
	 */
	public void toggleCDISupport(Boolean checked){
		log.info("Toogle CDI support to '" + String.valueOf(checked) + "'");
		new LabeledCheckBox("CDI support:").toggle(checked);
	}
	
	/**
	 * Returns true when Check Box 'CDI support' is checked.
	 *
	 * @return true, if is checked
	 */
	public boolean isCDISupport(){
		return new LabeledCheckBox("CDI support:").isChecked();
	}
}
