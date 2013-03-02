package org.jboss.tools.bpel.ui.bot.ext.activity;

import org.jboss.tools.bpel.ui.bot.ext.view.BPELPropertiesView;

/**
 * 
 * @author apodhrad
 * 
 */
public class RepeatUntil extends ContainerActivity {

	public RepeatUntil(String name) {
		super(name, "RepeatUntil");
	}

	public RepeatUntil setCondition(String condition) {
		BPELPropertiesView properties = new BPELPropertiesView();
		properties.setCondition(condition);
		return this;
	}

}
