package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.DefaultText;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitWhile;
import org.jboss.tools.switchyard.reddeer.widget.Link;
import org.jboss.tools.switchyard.reddeer.widget.RadioButton;

/**
 * 
 * @author apodhrad
 * 
 */
public class ServiceWizard<T extends ServiceWizard<?>> extends WizardDialog {

	private String dialogTitle;

	public ServiceWizard() {
		super();
	}

	public ServiceWizard(String dialogTitle) {
		super();
		this.dialogTitle = dialogTitle;
	}

	@SuppressWarnings("unchecked")
	public T activate() {
		Bot.get().shell(dialogTitle).activate();
		Bot.get().sleep(1000);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T setServiceName(String serviceName) {
		new LabeledText("Service Name:").setText(serviceName);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T checkInterfaceType(String interfaceType) {
		Bot.get().sleep(1000);
		new RadioButton(interfaceType).click();
		Bot.get().sleep(1000);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T selectJavaInterface(String javaInterface) {
		checkInterfaceType("Java").browse();
		new DefaultText().setText(javaInterface);
		clickOK();
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T createJavaInterface(String javaInterface) {
		checkInterfaceType("Java").clickInterface();
		new JavaInterfaceWizard().activate().setName(javaInterface).finish();
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T selectWSDLInterface(String wsdlInterface) {
		checkInterfaceType("WSDL").browse();
		new DefaultText().setText(wsdlInterface);
		clickOK();
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T createWSDLInterface(String wsdlInterface) {
		checkInterfaceType("WSDL").clickInterface();
		new Java2WSDLWizard().activate().finish();
		return (T) this;
	}

	public void clickInterface() {
		new Link("Interface:").click();
	}
	
	private void clickOK() {
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
		new PushButton("OK").click();
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

	protected void browse() {
		throw new UnsupportedOperationException("You have to implement browse() method!");
	}

}
