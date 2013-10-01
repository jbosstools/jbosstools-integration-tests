package org.jboss.tools.switchyard.reddeer.wizard;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.switchyard.reddeer.widget.Link;

public class BPMServiceWizard extends NewWizardDialog {
	
	public static final String DIALOG_TITLE = "New File";

	private String interfaceName;
	//private String processName;
	private String bpmnFileName;
	private static SWTWorkbenchBot bot = new SWTWorkbenchBot(); 

	public BPMServiceWizard() {
		super("SwitchYard", "SwitchYard BPM Component");//? where is this used? - in topmenuwizard.open(), but the component can't be created this way
	}

	/*public void setProcessName(String processName) {
		this.processName = processName;
	}*/

	public BPMServiceWizard activate() {
		bot.shell(DIALOG_TITLE).activate();
		return this;
	}

	public BPMServiceWizard setInterface(String name) {
		this.interfaceName = name;
		return this;
	}
	
	public BPMServiceWizard setFileName(String bpmnFileName) {
		this.bpmnFileName = bpmnFileName;
		return this;
	}

	@Override
	public void finish() {
		activate();
		if (bpmnFileName != null){
			new LabeledText("File name:").setText(bpmnFileName);
		}
		if (interfaceName != null) {
			new Link("Interface:").click();
			new JavaInterfaceWizard().activate().setName(interfaceName).finish();
			activate();
		}
		super.finish();
	}

}
