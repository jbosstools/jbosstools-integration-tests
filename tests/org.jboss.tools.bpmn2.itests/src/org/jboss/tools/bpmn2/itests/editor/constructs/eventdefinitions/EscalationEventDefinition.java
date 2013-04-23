package org.jboss.tools.bpmn2.itests.editor.constructs.eventdefinitions;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.bpmn2.itests.editor.constructs.AbstractEventDefinition;
import org.jboss.tools.bpmn2.itests.editor.properties.datatypes.AbstractDataType;

public class EscalationEventDefinition extends AbstractEventDefinition {

	private String escalationName;
	
	private String escalationCode;
	
	private AbstractDataType escalationDataType;
	
	public EscalationEventDefinition(String escalationName, String escalationCode, AbstractDataType escalationDataType) {
		this(escalationName, escalationCode, escalationDataType, 0);
	}
	
	public EscalationEventDefinition(String escalationName, String escalationCode, AbstractDataType escalationDataType ,int index) {
		super("Escalation Event Definition", index);
		
		this.escalationName = escalationName;
		this.escalationCode = escalationCode;
		this.escalationDataType = escalationDataType;
	}
	
	@Override
	protected void setUpDefinition() {
		new PushButton(0).click();
		
		SWTBot dialogBot = Bot.get().shell("Create New Escalation").bot();
		dialogBot.textWithLabel("Name").setText(escalationName);
		dialogBot.textWithLabel("Escalation Code").setText(escalationCode);
		dialogBot.button(0).click();
		
		escalationDataType.add();
		
		dialogBot.button("OK").click();
		
		bot.toolbarButtonWithTooltip("Close").click();
	}
	
}
