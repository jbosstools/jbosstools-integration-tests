package org.jboss.tools.switchyard.reddeer.binding;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.eclipse.jface.wizard.WizardPage;

/**
 * Binding Wizard
 * 
 * @author apodhrad
 * 
 */
public class BindingWizard<T extends WizardPage> extends WizardDialog {

	private T bindingWizardPage;

	public BindingWizard(T bindingWizardPage) {
		super();
		this.bindingWizardPage = bindingWizardPage;
	}

	public T getBindingPage() {
		return bindingWizardPage;
	}

	public MessageComposerPage getMessageComposerPage() {
		return new MessageComposerPage();
	}

	public static BindingWizard<CamelBindingPage> createCamelBindingWizard() {
		return new BindingWizard<CamelBindingPage>(new CamelBindingPage());
	}

	public static BindingWizard<FTPBindingPage> createFTPBindingWizard() {
		return new BindingWizard<FTPBindingPage>(new FTPBindingPage());
	}

	public static BindingWizard<FTPSBindingPage> createFTPSBindingWizard() {
		return new BindingWizard<FTPSBindingPage>(new FTPSBindingPage());
	}

	public static BindingWizard<FileBindingPage> createFileBindingWizard() {
		return new BindingWizard<FileBindingPage>(new FileBindingPage());
	}

	public static BindingWizard<JMSBindingPage> createJMSBindingWizard() {
		return new BindingWizard<JMSBindingPage>(new JMSBindingPage());
	}

	public static BindingWizard<JPABindingPage> createJPABindingWizard() {
		return new BindingWizard<JPABindingPage>(new JPABindingPage());
	}

	public static BindingWizard<MailBindingPage> createMailBindingWizard() {
		return new BindingWizard<MailBindingPage>(new MailBindingPage());
	}

	public static BindingWizard<NettyTCPBindingPage> createNettyTCPBindingWizard() {
		return new BindingWizard<NettyTCPBindingPage>(new NettyTCPBindingPage());
	}

	public static BindingWizard<NettyUDPBindingPage> createNettyUDPBindingWizard() {
		return new BindingWizard<NettyUDPBindingPage>(new NettyUDPBindingPage());
	}

	public static BindingWizard<HTTPBindingPage> createHTTPBindingWizard() {
		return new BindingWizard<HTTPBindingPage>(new HTTPBindingPage());
	}

	public static BindingWizard<RESTBindingPage> createRESTBindingWizard() {
		return new BindingWizard<RESTBindingPage>(new RESTBindingPage());
	}

	public static BindingWizard<SOAPBindingPage> createSOAPBindingWizard() {
		return new BindingWizard<SOAPBindingPage>(new SOAPBindingPage());
	}

	public static BindingWizard<SFTPBindingPage> createSFTPBindingWizard() {
		return new BindingWizard<SFTPBindingPage>(new SFTPBindingPage());
	}

	public static BindingWizard<SCABindingPage> createSCABindingWizard() {
		return new BindingWizard<SCABindingPage>(new SCABindingPage());
	}

	public static BindingWizard<SQLBindingPage> createSQLBindingWizard() {
		return new BindingWizard<SQLBindingPage>(new SQLBindingPage());
	}

	public static BindingWizard<SchedulingBindingPage> createSchedulingBindingWizard() {
		return new BindingWizard<SchedulingBindingPage>(new SchedulingBindingPage());
	}
}
