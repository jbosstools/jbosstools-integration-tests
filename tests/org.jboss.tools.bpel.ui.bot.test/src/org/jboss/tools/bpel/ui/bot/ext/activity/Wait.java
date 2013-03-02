package org.jboss.tools.bpel.ui.bot.ext.activity;

import org.jboss.tools.bpel.ui.bot.ext.view.BPELPropertiesView;

/**
 * 
 * @author apodhrad
 * 
 */
public class Wait extends Activity {

	public Wait(String name) {
		super(name, WAIT);
	}

	public Wait setCondition(String condition, String conditionType) {
		BPELPropertiesView properties = new BPELPropertiesView();
		properties.setCondition(condition, conditionType);
		bpelEditor.save();
		return this;
	}

}
