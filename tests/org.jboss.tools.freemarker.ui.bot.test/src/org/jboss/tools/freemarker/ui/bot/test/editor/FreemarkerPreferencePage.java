/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.freemarker.ui.bot.test.editor;

import org.eclipse.reddeer.core.reference.ReferencedComposite;
import org.eclipse.reddeer.jface.preference.PreferencePage;
import org.eclipse.reddeer.swt.impl.button.CheckBox;
import org.eclipse.reddeer.swt.impl.button.RadioButton;
import org.eclipse.reddeer.swt.impl.group.DefaultGroup;


/**
 * Freemarker preference page RedDeer implementation
 * @author Jiri Peterka
 *
 */
public class FreemarkerPreferencePage extends PreferencePage {
	

	/**
	 * Freemarker preference page
	 */
	public FreemarkerPreferencePage(ReferencedComposite composite) {
		super(composite, "FreeMarker");
	}

	/**
	 * Sets Freemarker highlight related directives to given value
	 */
	public void setHighLightRelatedDirectives(boolean value) {
		CheckBox cb = new CheckBox();
		cb.toggle(value);
	}

	/**
	 * Gets Freemarker highlight related directives to given value
	 * @return Freemarker highlight related directives 
	 */
	public boolean getHighLightRelatedDirectives() {
		CheckBox b = new CheckBox();
		return b.isChecked();		
	}
	
	/**
	 * Sets Freemarker outline level of details
	 * @param olod given details level
	 */
	public void setOutlineLevelOfDetail(OutlineLevelOfDetail olod) {		
		DefaultGroup group = new DefaultGroup("Outline Level of Detail:");
		switch (olod) {
		case FULL:
			new RadioButton(group, "Full").click();
			break;
		case FUNCTION_AND_MACRO_DEFINITIONS_ONLY:
			new RadioButton(group, "Function and Macro definitions only").click();
			break;
		default:
			break; // do nothing
		}		
	}

}
