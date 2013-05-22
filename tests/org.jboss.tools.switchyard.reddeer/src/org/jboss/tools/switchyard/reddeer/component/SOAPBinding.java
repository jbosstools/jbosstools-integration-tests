package org.jboss.tools.switchyard.reddeer.component;

import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * SOAP binding.
 * 
 * @author apodhrad
 *
 */
public class SOAPBinding extends Binding {

	private String contextPath;

	public SOAPBinding() {
		super("SOAP");
	}

	public SOAPBinding setContextpath(String contextPath) {
		this.contextPath = contextPath;
		return this;
	}

	@Override
	public void finish() {
		if (contextPath != null) {
			new LabeledText("Context Path").setText(contextPath);
		}
		super.finish();
	}

}
