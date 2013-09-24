package org.jboss.tools.switchyard.reddeer.component;

import org.jboss.tools.switchyard.reddeer.editor.SwitchYardEditor;
import org.jboss.tools.switchyard.reddeer.wizard.BPMServiceWizard;

public class BPM extends Component implements ComponentBuilder<BPM> {
	private String tooltip;
	private String service;
	private String bpmnFileName;//name of BPMN process

	public BPM() {
		super();
	}

	public BPM(String tooltip, int index) {
		super(tooltip, index);
	}

	public BPM(String tooltip) {
		super(tooltip);
	}

	public BPM setBPM(String BPM) {
		this.tooltip = BPM;
		return this;
	}

	public BPM setService(String service) {
		this.service = service;
		if (this.tooltip == null){
			this.tooltip = service;
		}
		return this;
	}
	
	public BPM setBpmnFileName(String bpmnFileName) {
		this.bpmnFileName = bpmnFileName;
		return this;
	}

	@Override
	public BPM create() {
		if (service == null) {
			throw new IllegalStateException("Service name is not defined!");
		}

		new SwitchYardEditor().addComponent("Process (BPMN)");
		new BPMServiceWizard().setInterface(service).setFileName(bpmnFileName).finish();
		new SwitchYardEditor().save();

		return new BPM();
	}
	
	/**
	 * 
	 * @param coords x, y coords
	 * @return
	 */
	public BPM create(Integer[] coords) {
		if (service == null) {
			throw new IllegalStateException("Service name is not defined!");
		}

		new SwitchYardEditor().addComponent("Process (BPMN)", coords);
		new BPMServiceWizard().setInterface(service).setFileName(bpmnFileName).finish();
		new SwitchYardEditor().save();

		return new BPM();
	}

}
