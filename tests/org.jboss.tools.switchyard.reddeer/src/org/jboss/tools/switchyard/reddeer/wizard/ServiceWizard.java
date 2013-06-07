package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.switchyard.reddeer.widget.RadioButton;

/**
 * 
 * @author apodhrad
 * 
 */
public class ServiceWizard extends WizardDialog {

	public ServiceWizard() {
		super();
	}

	public ServiceWizard setServiceName(String serviceName) {
		new LabeledText("Service Name:").setText(serviceName);
		return this;
	}

	public ServiceWizard checkInterfaceType(String interfaceType) {
		new RadioButton(interfaceType).click();
		return this;
	}

	public ServiceWizard selectJavaInterface(String javaInterface) {
		checkInterfaceType("Java");
		throw new UnsupportedOperationException("selectJavaInterface");
	}

	public ServiceWizard selectWSDLInterface(String wsdlInterface) {
		checkInterfaceType("WSDL").browse();
		new DefaultText().setText(wsdlInterface);
		new PushButton("OK").click();
		return this;
	}

	protected void browse() {
		throw new UnsupportedOperationException("You have to implement browse() method!");
	}

}
