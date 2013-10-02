package org.jboss.tools.bpmn2.itests.editor.jbpm.activities;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCombo;

import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.itests.editor.AbstractTask;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.IParameterMapping;


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
	
	/**
	 * 
	 * @param implementation
	 */
	public void setImplementation(String implementation) {
		properties.selectTab("Receive Task");
		new LabeledText("Implementation").setText(implementation);
	}
	
	/**
	 * 
	 * @param operation
	 */
	public void setOperation(String operation, String inMessage, String outMessage) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * TODO: Add browse and add import or will we use a predefined project?
	 * 
	 * @param name
	 * @param dataType
	 */
	public void setMessage(String name, String dataType) {
		properties.selectTab("Receive Task");
		
		SWTBotCombo messageBox = bot.comboBoxWithLabel("Message");
		String messageName = name + "(" + dataType + ")";
		if (properties.contains(messageBox, messageName)) {
			messageBox.setSelection(messageName);
		} else {
			// PushButton(0) denotes operation
			new PushButton(1).click();
			
			SWTBot newMessageBot = bot.shell("Create New Message").bot();
			newMessageBot.textWithLabel("Name").setText(name);
			newMessageBot.button(0).click();
			
			SWTBot newDataTypeBot = bot.shell("Create New Data Type").bot();
			newDataTypeBot.textWithLabel("Structure").setText(dataType);
			newDataTypeBot.button("OK").click();

			newMessageBot.button("OK").click();
		}
		
	}
	
	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.AbstractTask#setIsForCompensation(boolean)
	 */
	@Override
	public void setIsForCompensation(boolean b) {
		super.setIsForCompensation(b);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.AbstractTask#setOnEntryScript(java.lang.String, java.lang.String)
	 */
	@Override
	public void setOnEntryScript(String language, String script) {
		super.setOnEntryScript(language, script);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.AbstractTask#setOnExistScript(java.lang.String, java.lang.String)
	 */
	@Override
	public void setOnExistScript(String language, String script) {
		super.setOnExistScript(language, script);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.AbstractTask#addInputParameter(org.jboss.tools.bpmn2.itests.editor.IParameterMapping)
	 */
	@Override
	public void addParameterMapping(IParameterMapping parameter) {
		super.addParameterMapping(parameter);
	}

}
