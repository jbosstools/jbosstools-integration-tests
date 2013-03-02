package org.jboss.tools.bpel.ui.bot.ext.activity;

import org.jboss.tools.bpel.ui.bot.ext.view.BPELPropertiesView;

/**
 * 
 * @author apodhrad
 * 
 */
public class Reply extends Activity {

	public Reply(String name) {
		super(name, REPLY);
	}

	public Reply pickOperation(String operation) {
		BPELPropertiesView properties = new BPELPropertiesView();
		properties.pickOperation(operation);
		bpelEditor.save();
		return this;
	}
}
