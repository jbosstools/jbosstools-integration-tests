package org.jboss.tools.bpel.reddeer.activity;

import org.jboss.reddeer.swt.util.Bot;
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
		Bot.get().ccomboBox().setSelection(targetActivity);
		Bot.get().ccomboBox().setFocus();
		bpelEditor.save();
		return this;
	}
}
