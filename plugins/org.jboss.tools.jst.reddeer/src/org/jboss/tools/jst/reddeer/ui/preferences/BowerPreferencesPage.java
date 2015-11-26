/******************************************************************************* 
 * Copyright (c) 2014 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.reddeer.ui.preferences;

import org.jboss.reddeer.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.api.Text;
import org.jboss.reddeer.swt.api.Button;

/**
 * RedDeer implementation of JBoss Tools > Bower Preferences page 
 * @author Pavol Srna
 *
 */
public class BowerPreferencesPage extends PreferencePage {
	
	public static final String PAGE_NAME = "Bower";
	
	public BowerPreferencesPage(){
		super("JBoss Tools", PAGE_NAME);
	}

	public Text getNodeLocation(){
		return new LabeledText("Node Location");
	}
	
	public Text getBowerLocation(){
		return new LabeledText("Bower Location");
	}
	
	public Button getOkBtn(){
		return new PushButton("OK");
	}
	
	public Button getCancelBtn(){
		return new PushButton("Cancel");
	}
	
	public Button getApplyBtn(){
		return new PushButton("Apply");
	}
}
