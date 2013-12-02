package org.jboss.tools.cdi.reddeer.common.model.ui;

import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.button.RadioButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.wait.WaitWhile;

/**
 * Represents dialog invoked when manipulating in JBT beans.xml editor
 * with Scan object
 * 
 * @author jjankovi
 *
 */
public class AddIncludeExcludeDialog extends DefaultShell {

	public AddIncludeExcludeDialog() {
		super("Add Include/Exclude");
	}
	
	public void include() {
		new RadioButton("include").click();
	}
	
	/** not WORKING cause an upstream issue with RadioButton click **/
	public void exclude() {
		new RadioButton("exclude").click();
	}
	
	public void setName(String name) {
		/*
		 * Text with label "Name:*".
		 * Text and Label do not have common parent SWT hierarchy; they have common grandparent.
		 * LabeledText("Name:*") can not be used.
		 */
		new DefaultText(0).setText(name);
	}
	
	public void setRegularExpressionState(boolean isRegular) {
		new CheckBox().toggle(isRegular);
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
