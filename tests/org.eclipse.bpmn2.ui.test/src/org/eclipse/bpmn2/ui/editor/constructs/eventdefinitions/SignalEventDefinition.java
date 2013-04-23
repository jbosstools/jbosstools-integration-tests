package org.eclipse.bpmn2.ui.editor.constructs.eventdefinitions;

import org.eclipse.bpmn2.ui.editor.constructs.AbstractEventDefinition;
import org.eclipse.bpmn2.ui.editor.properties.datatypes.AbstractDataType;
import org.eclipse.swtbot.swt.finder.SWTBot;

import org.jboss.reddeer.swt.util.Bot;

public class SignalEventDefinition extends AbstractEventDefinition {
	
	private String signalName;
	
	private AbstractDataType signalDataType;
	
	public SignalEventDefinition(String signalName, AbstractDataType signalDataType) {
		this(signalName, signalDataType, 0);
	}
	
	public SignalEventDefinition(String signalName, AbstractDataType signalDataType, int index) {
		super("Signal Event Definition", index);
		
		this.signalName = signalName;
		this.signalDataType = signalDataType;
	}

	@Override
	protected void setUpDefinition() {
		bot.button(0).click();
		
		SWTBot dialogBot = Bot.get().shell("Create New Signal").bot();
		dialogBot.textWithLabel("Name").setText(signalName);
		dialogBot.button(0).click();
		signalDataType.add();
		dialogBot.button("OK").click();
		
		bot.toolbarButtonWithTooltip("Close").click();
	}
	
}
