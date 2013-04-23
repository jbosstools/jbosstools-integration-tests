package org.jboss.tools.bpmn2.itests.editor.constructs.tasks;

import org.eclipse.swtbot.swt.finder.SWTBot;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.constructs.AbstractTask;
import org.jboss.tools.bpmn2.itests.editor.properties.variables.IParameter;


/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class ReceiveTask extends AbstractTask {

	/**
	 * 
	 * @param name
	 */
	public ReceiveTask(String name) {
		super(name, ConstructType.RECEIVE_TASK);
	}
	
	public void setRuleFlowGroup(String group) {
		properties.selectTab("Receive Task");
		new LabeledText("Rule Flow Group").setText(group);
	}
	
	/**
	 * TODO: Add browse and add import or will we use a predefined project?
	 * 
	 * @param name
	 * @param dataType
	 */
	public void setMessage(String name, String dataType) {
		properties.selectTab("Receive Task");
		new PushButton(0).click();
		
		SWTBot newMessageBot = Bot.get().shell("Create New Message").bot();
		newMessageBot.textWithLabel("Name").setText(name);
		newMessageBot.button(0).click();
		
		SWTBot newDataTypeBot = Bot.get().shell("Create New Data Type").bot();
		newDataTypeBot.textWithLabel("Data Type").setText(dataType);
		newDataTypeBot.button("OK").click();
		
		newMessageBot.button("OK").click();
	}
	
	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractTask#setIsForCompensation(boolean)
	 */
	@Override
	public void setIsForCompensation(boolean b) {
		super.setIsForCompensation(b);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractTask#setOnEntryScript(java.lang.String, java.lang.String)
	 */
	@Override
	public void setOnEntryScript(String language, String script) {
		super.setOnEntryScript(language, script);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractTask#setOnExistScript(java.lang.String, java.lang.String)
	 */
	@Override
	public void setOnExistScript(String language, String script) {
		super.setOnExistScript(language, script);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractTask#addInputParameter(org.jboss.tools.bpmn2.itests.editor.properties.variables.IParameter)
	 */
	@Override
	public void addParameterMapping(IParameter parameter) {
		super.addParameterMapping(parameter);
	}

}
