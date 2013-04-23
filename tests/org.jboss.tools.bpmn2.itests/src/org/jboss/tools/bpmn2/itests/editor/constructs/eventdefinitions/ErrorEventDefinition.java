package org.jboss.tools.bpmn2.itests.editor.constructs.eventdefinitions;

import org.eclipse.swtbot.swt.finder.SWTBot;

import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.bpmn2.itests.editor.constructs.AbstractEventDefinition;
import org.jboss.tools.bpmn2.itests.editor.properties.datatypes.AbstractDataType;

/**
 * 
 * @author
 */
public class ErrorEventDefinition extends AbstractEventDefinition {

	private String errorName;
	
	private String errorCode;
	
	private AbstractDataType errorDataType;
	
	public ErrorEventDefinition(String errorName, String errorCode, AbstractDataType errorDataType) {
		this(errorName, errorCode, errorDataType, 0);
	}
	
	public ErrorEventDefinition(String errorName, String errorCode, AbstractDataType errorDataType, int index) {
		super("Error Event Definition", index);
		
		this.errorName = errorName;
		this.errorCode = errorCode;
		this.errorDataType = errorDataType;
	}
	
	@Override
	protected void setUpDefinition() {
		bot.button(0).click();

		SWTBot dialogBot = Bot.get().shell("Create New Error").bot();
		dialogBot.textWithLabel("Name").setText(errorName);
		dialogBot.textWithLabel("Error Code").setText(errorCode);
		dialogBot.button(0).click();
		
		errorDataType.add();
		
		dialogBot.button("OK").click();
		
		bot.toolbarButtonWithTooltip("Close").click();
	}
	
}
