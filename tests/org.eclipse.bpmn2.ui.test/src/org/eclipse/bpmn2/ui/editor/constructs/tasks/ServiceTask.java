package org.eclipse.bpmn2.ui.editor.constructs.tasks;

import org.eclipse.bpmn2.ui.editor.ConstructType;
import org.eclipse.bpmn2.ui.editor.constructs.AbstractTask;
import org.eclipse.bpmn2.ui.editor.properties.variables.IParameter;

import org.eclipse.swtbot.swt.finder.SWTBot;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.util.Bot;


/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class ServiceTask extends AbstractTask {

	/**
	 * 
	 * @param name
	 */
	public ServiceTask(String name) {
		super(name, ConstructType.SERVICE_TASK);
	}
	
	public void setOperation(String name) {
		properties.selectTab("Service Task");
		
		new PushButton(0).click();
		SWTBot viewBot = Bot.get().shell("Create New Operation").bot();
		viewBot.textWithLabel("Name").setText(name);
		viewBot.button("OK").click();
	}

	/**
	 * @see org.eclipse.bpmn2.ui.editor.constructs.AbstractTask#setIsForCompensation(boolean)
	 */
	@Override
	public void setIsForCompensation(boolean b) {
		super.setIsForCompensation(b);
	}

	/**
	 * @see org.eclipse.bpmn2.ui.editor.constructs.AbstractTask#addInputParameter(org.eclipse.bpmn2.ui.editor.properties.variables.IParameter)
	 */
	@Override
	public void addParameterMapping(IParameter parameter) {
		super.addParameterMapping(parameter);
	}

}
