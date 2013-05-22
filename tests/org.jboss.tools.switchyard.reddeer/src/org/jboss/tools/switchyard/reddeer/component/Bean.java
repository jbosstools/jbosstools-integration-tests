package org.jboss.tools.switchyard.reddeer.component;

import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.wizard.BeanServiceWizard;

/**
 * A bean component.
 * 
 * @author apodhrad
 * 
 */
public class Bean extends Component implements ComponentBuilder<Bean> {

	private String bean;
	private String service;

	public Bean() {
		super();
	}

	public Bean(String tooltip, int index) {
		super(tooltip, index);
	}

	public Bean(String tooltip) {
		super(tooltip);
	}

	public Bean setBean(String bean) {
		this.bean = bean;
		return this;
	}

	public Bean setService(String service) {
		this.service = service;
		if (bean == null) {
			bean = service + "Bean";
		}
		return this;
	}

	@Override
	public Bean create() {
		if (service == null) {
			throw new IllegalStateException("Service name is not defined!");
		}

		new SwitchYardEditor().addComponent("Bean");
		new BeanServiceWizard().setInterface(service).finish();
		new SwitchYardEditor().save();

		return new Bean(bean);
	}

}
