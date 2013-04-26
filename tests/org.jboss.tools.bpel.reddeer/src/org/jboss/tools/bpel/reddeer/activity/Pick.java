package org.jboss.tools.bpel.reddeer.activity;

import org.jboss.tools.bpel.reddeer.view.BPELPropertiesView;

/**
 * 
 * @author apodhrad
 * 
 */
public class Pick extends Activity {

	public Pick(String name) {
		super(name, PICK);
	}

	public Pick checkCreateInstance() {
		BPELPropertiesView properties = new BPELPropertiesView();
		properties.toggleCreateInstance(true);
		bpelEditor.save();
		return this;
	}

	public OnMessage addOnMessage() {
		menu("Add OnMessage");
		return new OnMessage(this);
	}

	public OnAlarm addOnAlarm() {
		menu("Add OnAlarm");
		return new OnAlarm(this);
	}

}
