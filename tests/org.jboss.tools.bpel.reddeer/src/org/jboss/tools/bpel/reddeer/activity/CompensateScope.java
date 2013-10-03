package org.jboss.tools.bpel.reddeer.activity;

import org.jboss.tools.bpel.reddeer.view.BPELPropertiesView;

/**
 * 
 * @author apodhrad
 * 
 */
public class CompensateScope extends Activity {

	public CompensateScope(String name) {
		super(name, COMPENSATE_SCOPE);
	}

	public CompensateScope setTargetActivity(String targetActivity) {
		BPELPropertiesView properties = new BPELPropertiesView();
		properties.selectDetails();
		bot.ccomboBox().setSelection(targetActivity);
		bot.ccomboBox().setFocus();
		bpelEditor.save();
		return this;
	}
}
