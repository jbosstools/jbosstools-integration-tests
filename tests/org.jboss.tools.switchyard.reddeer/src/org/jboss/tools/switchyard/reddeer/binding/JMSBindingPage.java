package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * JMS binding page
 * 
 * @author apodhrad
 * 
 */
public class JMSBindingPage extends OperationOptionsPage<JMSBindingPage> {

	public static final String NAME = "Name";
	public static final String QUEUE_TOPIC_NAME = "Queue/Topic Name";

	public JMSBindingPage setName(String name) {
		new LabeledText(NAME).setFocus();
		new LabeledText(NAME).setText(name);
		return this;
	}

	public String getName() {
		return new LabeledText(NAME).getText();
	}

	public JMSBindingPage setQueueTopicName(String name) {
		new LabeledText(QUEUE_TOPIC_NAME).setFocus();
		new LabeledText(QUEUE_TOPIC_NAME).setText(name);
		return this;
	}

	public String getQueueTopicName() {
		return new LabeledText(QUEUE_TOPIC_NAME).getText();
	}
}
