package org.jboss.tools.cdi.reddeer.common.model.ui;

import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.WaitWhile;

/**
 * Represents dialog invoked when manipulating in JBT beans.xml editor
 * with include/exclude property
 * 
 * @author jjankovi
 *
 */
public class AddIfSystemPropertyDialog extends DefaultShell {

	public AddIfSystemPropertyDialog() {
		super("Add If System Property");
	}
	
	public void setName(String name) {
		new LabeledText("Name:*").setText(name);
	}
	
	public void setValue(String value) {
		new LabeledText("Value:").setText(value);
	}
	
	public void cancel() {
		new PushButton("Cancel").click();
	}
	
	public void finish() {
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsActive(this.getText()));
	}
	
}
