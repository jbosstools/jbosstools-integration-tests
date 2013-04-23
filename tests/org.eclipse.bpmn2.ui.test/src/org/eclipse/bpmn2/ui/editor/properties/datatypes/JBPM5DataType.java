package org.eclipse.bpmn2.ui.editor.properties.datatypes;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.reddeer.swt.util.Bot;

public class JBPM5DataType extends AbstractDataType {

	private int index;
	
	public JBPM5DataType(String name) {
		this(name, 0);
	}
	
	public JBPM5DataType(String name, int index) {
		super(name);

		this.index = index;
	}

	/*
	public void add() {
		SWTBot bot = Bot.get();
		bot.toolbarButtonWithTooltip("Add", index).click();
		bot.textWithLabel("Data Type").setText(name);
		bot.toolbarButtonWithTooltip("Close").click();
	}
	*/

	public void add() {
		SWTBot bot = Bot.get();
		bot.button(index).click();

		SWTBot dialogBot = Bot.get().shell("Create New Data Type").bot();
		dialogBot.textWithLabel("Data Type").setText(typeName);
		dialogBot.button("OK").click();
		
		bot.toolbarButtonWithTooltip("Close").click();
	}
	
}
