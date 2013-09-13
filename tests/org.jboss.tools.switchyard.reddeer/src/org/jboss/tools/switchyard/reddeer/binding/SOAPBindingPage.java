package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;

/**
 * SOAP binding page
 * 
 * @author apodhrad
 * 
 */
public class SOAPBindingPage extends WizardPage {

	public static final String NAME = "Name";
	public static final String CONTEXT_PATH = "Context Path";
	public static final String WSDL_URI = "WSDL URI";

	public SOAPBindingPage setContextPath(String contextPath) {
		new LabeledText(CONTEXT_PATH).setFocus();
		new LabeledText(CONTEXT_PATH).setText(contextPath);
		new LabeledText(NAME).setFocus();
		return this;
	}

	public String getContextPath() {
		return new LabeledText(CONTEXT_PATH).getText();
	}

	public SOAPBindingPage setWsdlURI(String uri) {
		new DefaultText(1).setFocus();
		new DefaultText(1).setText(uri);
		new LabeledText(NAME).setFocus();
		return this;
	}

	public String getWsdlURI() {
		throw new UnsupportedOperationException();
	}

}
