package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * Scheduling binding page
 * 
 * @author apodhrad
 * 
 */
public class SchedulingBindingPage extends OperationOptionsPage<SchedulingBindingPage> {

	public static final String NAME = "Name*";
	public static final String CRON = "Cron*";

	public SchedulingBindingPage setName(String name) {
		new LabeledText(NAME).setFocus();
		new LabeledText(NAME).setText(name);
		return this;
	}

	public String getName() {
		return new LabeledText(NAME).getText();
	}

	public SchedulingBindingPage setCron(String cron) {
		new LabeledText(CRON).setFocus();
		new LabeledText(CRON).setText(cron);
		new LabeledText(NAME).setFocus();
		return this;
	}

	public String getCron() {
		return new LabeledText(CRON).getText();
	}

}
