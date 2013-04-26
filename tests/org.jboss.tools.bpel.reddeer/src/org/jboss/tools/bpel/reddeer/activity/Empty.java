package org.jboss.tools.bpel.reddeer.activity;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.tools.bpel.reddeer.view.BPELPropertiesView;

/**
 * 
 * @author apodhrad
 * 
 */
public class Empty extends Activity {

	public Empty(String name) {
		super(name, EMPTY);
	}

	public Invoke toInvoke() {
		changeTo("1");
		return new Invoke(name);
	}

	public Receive toReceive() {
		changeTo("2");
		return new Receive(name);
	}

	public Reply toReply() {
		changeTo("3");
		return new Reply(name);
	}

	public Assign toAssign() {
		changeTo("4");
		return new Assign(name);
	}

	private void changeTo(String num) {
		BPELPropertiesView properties = new BPELPropertiesView();
		properties.selectDetails();
		new PushButton(num).click();
	}
}
