package org.jboss.tools.cdi.reddeer.common.model.ui;

import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.wait.WaitWhile;

/**
 * Represents dialog invoked when manipulating in JBT beans.xml editor
 * with include/exclude property
 * 
 * @author jjankovi
 *
 */
public class AddIfClassAvailableDialog extends DefaultShell {

	public AddIfClassAvailableDialog() {
		super("Add If Class Available");
	}
	
	public void setName(String name) {
		/*
		 * Text labeled "Name:*", no direct common parent -> LabeledText can't be used
		 */
		new DefaultText(0).setText(name);
	}
	
	public void cancel() {
		new PushButton("Cancel").click();
	}
	
	public void finish() {
		String shellText = getText();
		new PushButton("Finish").click();
		new WaitWhile(new ShellWithTextIsActive(shellText));
	}
	
}
