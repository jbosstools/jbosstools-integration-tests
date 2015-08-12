/*******************************************************************************
 * Copyright (c) 2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.reddeer.preferences;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.impl.button.CheckBox;
/**
 * RedDeer model of JBoss Tools > Web > Editors > Visual Page Editor preference page
 * 
 * @author vlado pakan
 */
public class VisualPageEditorPreferencePage extends PreferencePage  {
	
	protected final static Logger log = Logger.getLogger(VisualPageEditorPreferencePage.class);
	
	public VisualPageEditorPreferencePage() {
		super("JBoss Tools", "Web" , "Editors" , "Visual Page Editor");
	}
	/**
	 * Sets Do Not Show Browser Engine Dialog
	 * @param checked
	 */
	public void setDoNotShowBrowserEngineDialog (boolean checked){
		log.debug("Set Do not show Browser Engine dialog to: " + checked);
		new CheckBox("Do not show Browser Engine dialog").toggle(checked);
	}

}
