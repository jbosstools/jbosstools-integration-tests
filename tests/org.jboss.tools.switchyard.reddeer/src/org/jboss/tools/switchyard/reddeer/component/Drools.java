package org.jboss.tools.switchyard.reddeer.component;

import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.wizard.DroolsServiceWizard;

public class Drools extends Component implements ComponentBuilder<Drools>{
	private String tooltip;
	private String service;
	private String fileName;

	public Drools() {
		super();
	}

	public Drools(String tooltip, int index) {
		super(tooltip, index);
	}

	public Drools(String tooltip) {
		super(tooltip);
	}

	public Drools setName(String name) {
		this.tooltip = name;
		return this;
	}

	public Drools setService(String service) {
		this.service = service;
		if (this.tooltip == null){
			this.tooltip = service;
		}
		return this;
	}
	
	public Drools setfileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	@Override
	public Drools create() {
		if (service == null) {
			throw new IllegalStateException("Service name is not defined!");
		}

		new SwitchYardEditor().addComponent("Rules (DRL)");
		new DroolsServiceWizard().setInterface(service).setFileName(fileName).finish();
		new SwitchYardEditor().save();

		return new Drools();
	}
}
