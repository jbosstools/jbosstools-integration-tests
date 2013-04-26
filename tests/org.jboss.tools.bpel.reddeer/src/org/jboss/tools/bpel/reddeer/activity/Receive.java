package org.jboss.tools.bpel.reddeer.activity;

import org.jboss.tools.bpel.reddeer.view.BPELPropertiesView;

/**
 * 
 * @author apodhrad
 * 
 */
public class Receive extends Activity {

	public Receive(String name) {
		super(name, "Receive");
	}

	public Receive pickOperation(String operation) {
		BPELPropertiesView properties = new BPELPropertiesView();
		properties.pickOperation(operation);
		bpelEditor.save();
		return this;
	}

	public Receive checkCreateInstance() {
		BPELPropertiesView properties = new BPELPropertiesView();
		properties.toggleCreateInstance(true);
		bpelEditor.save();
		return this;
	}
}
