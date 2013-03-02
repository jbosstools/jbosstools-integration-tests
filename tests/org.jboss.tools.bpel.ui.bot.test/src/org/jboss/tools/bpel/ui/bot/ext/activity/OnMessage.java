package org.jboss.tools.bpel.ui.bot.ext.activity;

import org.jboss.tools.bpel.ui.bot.ext.view.BPELPropertiesView;

/**
 * 
 * @author apodhrad
 * 
 */
public class OnMessage extends ContainerActivity {

	public OnMessage(Activity parent) {
		this(null, parent);
	}

	public OnMessage(String name, Activity parent) {
		super(name, "OnMessage", parent, 0);
	}

	public OnMessage pickOperation(String operation) {
		BPELPropertiesView properties = new BPELPropertiesView();
		properties.pickOperation(operation);
		bpelEditor.save();
		return this;
	}
}
