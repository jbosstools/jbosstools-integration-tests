package org.jboss.tools.switchyard.reddeer.wizard;

import org.jboss.reddeer.eclipse.jface.wizard.WizardDialog;
import org.jboss.reddeer.swt.impl.combo.DefaultCombo;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.switchyard.reddeer.widget.Link;
import org.jboss.tools.switchyard.reddeer.widget.RadioButton;

/**
 * Wizard for promoting a service.
 * 
 * @author apodhrad
 * 
 */
public class PromoteServiceWizard extends WizardDialog {

	public static final String DIALOG_TITLE = "Promote Component Service";

	public static final String DEFAULT_INTERFACE_TYPE = "Java";
	public static final String DEFAULT_TRANSFORMER_TYPE = "Java Transformer";

	private String name;
	private String interfaceType;
	private String interfaceFile;
	private String transformerType;
	private String transformerName;

	public PromoteServiceWizard() {
		this.interfaceType = DEFAULT_INTERFACE_TYPE;
		this.transformerType = DEFAULT_TRANSFORMER_TYPE;
	}

	public PromoteServiceWizard setName(String name) {
		this.name = name;
		return this;
	}

	public PromoteServiceWizard setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
		return this;
	}

	public PromoteServiceWizard selectInterface(String interfaceFile) {
		this.interfaceFile = interfaceFile;
		return this;
	}

	public PromoteServiceWizard setInterfaceFile(String interfaceFile) {
		this.interfaceFile = interfaceFile;
		return this;
	}

	public PromoteServiceWizard setTransformerType(String transformerType) {
		this.transformerType = transformerType;
		return this;
	}

	public PromoteServiceWizard setTransformerName(String transformerName) {
		this.transformerName = transformerName;
		return this;
	}

	protected void createInterface() {
		new Link("Interface:").click();
		Java2WSDLWizard wizard = new Java2WSDLWizard();
		wizard.next();
		wizard.finish();
	}

	public void create() {
		Bot.get().shell(DIALOG_TITLE).activate();
		new RadioButton(interfaceType).click();
		if (interfaceFile == null) {
			createInterface();
		} else {
			selectInterface(interfaceFile);
		}
		Bot.get().shell(DIALOG_TITLE).activate();
		next();
		new DefaultCombo("Transformer Type:").setSelection(transformerType);
		next();
		new LabeledText("Name:").setText(transformerName);
		finish();
	}

	public void create2() {
		Bot.get().shell(DIALOG_TITLE).activate();
		if (name != null) {
			new LabeledText("Service Name:").setText(name);
		}
		finish();
	}
}
