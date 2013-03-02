package org.jboss.tools.bpel.ui.bot.ext.activity;

import org.jboss.tools.bpel.ui.bot.ext.view.BPELPropertiesView;

/**
 * 
 * @author apodhrad
 * 
 */
public class OnAlarm extends ContainerActivity {

	public OnAlarm(Activity parent) {
		super(null, "OnAlarm", parent, 0);
	}

	public OnAlarm setCondition(String condition, String conditionType) {
		BPELPropertiesView properties = new BPELPropertiesView();
		properties.setCondition(condition, conditionType);
		return this;
	}

}
