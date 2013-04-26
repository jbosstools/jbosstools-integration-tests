package org.jboss.tools.bpel.reddeer.activity;

import org.jboss.tools.bpel.reddeer.view.BPELPropertiesView;

/**
 * 
 * @author apodhrad
 * 
 */
public class Invoke extends Activity {

	public Invoke(String name) {
		super(name, INVOKE);
	}

	public Invoke pickOperation(String operation) {
		BPELPropertiesView properties = new BPELPropertiesView();
		properties.pickOperation(operation);
		bpelEditor.save();
		return this;
	}

}
