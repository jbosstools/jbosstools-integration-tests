package org.jboss.tools.bpel.reddeer.activity;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.tools.bpel.reddeer.view.BPELPropertiesView;

/**
 * 
 * @author apodhrad
 * 
 */
public class Validate extends Activity {

	public Validate(String name) {
		super(name, "Validate");
	}

	public Validate addVariable(String variable) {
		BPELPropertiesView properties = new BPELPropertiesView();
		properties.selectDetails();
		new PushButton("Add").click();
		new DefaultShell("Select Variable");
		new DefaultTable().select(variable);
		new PushButton("OK").click();
		return this;
	}
}
