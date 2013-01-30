package org.jboss.tools.freemarker.ui.bot.test;

import org.jboss.reddeer.eclipse.jface.preference.PreferencePage;
import org.jboss.reddeer.swt.impl.button.CheckBox;


public class FreemarkerPreferencePage extends PreferencePage {

	public FreemarkerPreferencePage() {
		super("FreeMarker Editor");		
	}
	
	public void setHighLightRelatedDirectives(boolean value) {
		CheckBox cb = new CheckBox("Highlight Related Directives");
		cb.toggle(value);
	}

	public boolean getHighLightRelatedDirectives() {
		CheckBox b = new CheckBox();
		return b.isChecked();		
	}

}
