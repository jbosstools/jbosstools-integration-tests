package org.jboss.tools.freemarker.ui.bot.test;

import org.jboss.reddeer.eclipse.jface.preference.WindowPreferencePage;
import org.jboss.reddeer.swt.impl.button.CheckBox;


public class FreemarkerPreferencePage extends WindowPreferencePage {

	public FreemarkerPreferencePage() {
		super("FreeMarker Editor");		
	}
	
	public void setHighLightRelatedDirectives(boolean value) {
		CheckBox cb = new CheckBox();
		cb.toggle(value);
	}

	public boolean getHighLightRelatedDirectives() {
		CheckBox b = new CheckBox();
		return b.isChecked();		
	}

}
