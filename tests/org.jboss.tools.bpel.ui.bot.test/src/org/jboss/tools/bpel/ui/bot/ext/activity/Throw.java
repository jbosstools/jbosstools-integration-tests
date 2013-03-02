package org.jboss.tools.bpel.ui.bot.ext.activity;

import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpel.ui.bot.ext.view.BPELPropertiesView;

/**
 * 
 * @author apodhrad
 * 
 */
public class Throw extends Activity {

	public Throw(String name) {
		super(name, THROW);
	}

	public void setUserFaultName(String name) {
		BPELPropertiesView properties = new BPELPropertiesView();
		properties.selectDetails();
		new RadioButton("User-defined").click();
		new LabeledText("Fault Name:").setText(name);
	}
}
