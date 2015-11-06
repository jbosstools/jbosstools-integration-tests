package org.jboss.tools.freemarker.ui.bot.test;

import org.jboss.reddeer.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;


/**
 * Freemarker preference page RedDeer implementation
 * @author Jiri Peterka
 *
 */
public class FreemarkerPreferencePage extends PreferencePage {
	

	/**
	 * Freemarker preference page
	 */
	public FreemarkerPreferencePage() {
		super(new String[] {"FreeMarker"});
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
