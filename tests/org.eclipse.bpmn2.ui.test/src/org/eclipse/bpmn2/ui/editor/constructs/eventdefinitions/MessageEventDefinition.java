package org.eclipse.bpmn2.ui.editor.constructs.eventdefinitions;

import org.eclipse.bpmn2.ui.editor.constructs.AbstractEventDefinition;
import org.eclipse.bpmn2.ui.editor.properties.datatypes.AbstractDataType;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.reddeer.swt.util.Bot;

public class MessageEventDefinition extends AbstractEventDefinition {

	private String operationName;
	
	private String messageName;
	
	private AbstractDataType messageDataType;
	
	public MessageEventDefinition(String operationName, String messageName, AbstractDataType messageDataType) {
		this(operationName, messageName, messageDataType, 0);
	}
	
	public MessageEventDefinition(String operationName, String messageName, AbstractDataType messageDataType, int index) {
		super("Message Event Definition", index);
		
		this.operationName = operationName;
		this.messageName = messageName;
		this.messageDataType = messageDataType;
	}
	
	@Override
	protected void setUpDefinition() {
		bot.comboBoxWithLabel("Operation").setSelection(operationName);
		bot.button(0).click();
		
		SWTBot dialogBot = Bot.get().shell("Create New Message").bot();
		dialogBot.textWithLabel("Name").setText(messageName);
		dialogBot.button(0).click();
		messageDataType.add();
		dialogBot.button("OK").click();
		
		bot.toolbarButtonWithTooltip("Close").click();
	}
	
}
