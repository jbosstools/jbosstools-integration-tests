package org.jboss.tools.bpel.reddeer.activity;

import org.jboss.tools.bpel.reddeer.view.BPELPropertiesView;

/**
 * 
 * @author apodhrad
 * 
 */
public class While extends ContainerActivity {

	public While(String name) {
		super(name, WHILE);
	}

	public While setCondition(String condition) {
		BPELPropertiesView properties = new BPELPropertiesView();
		properties.setCondition(condition);
		bpelEditor.save();
		return this;
	}

}
