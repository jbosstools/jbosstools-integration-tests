package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * HTTP binding.
 * 
 * @author apodhrad
 * 
 */
public class HTTPBindingPage extends OperationOptionsPage<HTTPBindingPage> {

	public static final String CONTEXT_PATH = "Context Path";

	public HTTPBindingPage setContextPath(String contextPath) {
		new LabeledText(CONTEXT_PATH).setFocus();
		new LabeledText(CONTEXT_PATH).setText(contextPath);
		return this;
	}
	
	public String getContextPath() {
		return new LabeledText(CONTEXT_PATH).getText();
	}

}
