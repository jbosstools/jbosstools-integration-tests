package org.jboss.tools.switchyard.reddeer.component;

import org.apache.log4j.Logger;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;

/**
 * 
 * @author apodhrad
 * 
 */
public abstract class Binding {

	protected final Logger log = Logger.getLogger(this.getClass());

	private String binding;

	public Binding(String binding) {
		this.binding = binding;
	}

	public String getBinding() {
		return binding;
	}

	public void activate() {
		new SwitchYardEditor().activateTool(binding);
	}

	public void finish() {
		log.info("Finish wizard");

		DefaultShell shell = new DefaultShell();
		new PushButton("Finish").click();

		new WaitWhile(new ShellWithTextIsActive(shell.getText()), TimePeriod.LONG);
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);

		new SwitchYardEditor().save();
	}
}
