package org.jboss.tools.freemarker.ui.bot.test;

import org.jboss.reddeer.workbench.preference.WorkbenchPreferencePage;
import org.jboss.reddeer.eclipse.jdt.ui.WorkbenchPreferenceDialog;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.group.DefaultGroup;


public class FreemarkerPreferencePage extends WorkbenchPreferencePage {

	public FreemarkerPreferencePage() {
		super("FreeMarker");		
	}
	
	public void setHighLightRelatedDirectives(boolean value) {
		CheckBox cb = new CheckBox();
		cb.toggle(value);
	}

	public boolean getHighLightRelatedDirectives() {
		CheckBox b = new CheckBox();
		return b.isChecked();		
	}
	
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
