package org.jboss.tools.bpmn2.itests.editor.jbpm;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.reddeer.swt.util.Bot;
import org.jboss.tools.bpmn2.itests.editor.IDataType;

public class DataType implements IDataType {

	private int index;
	
	private String name;
	
	public DataType(String name) {
		this(name, 0);
	}
	
	public DataType(String name, int index) {
		this.name = name;
		this.index = index;
	}
	
	public void add() {
		SWTBot bot = Bot.get();
//		bot.toolbarButtonWithTooltip("Add", index).click();
		bot.button(index).click();

		SWTBot dialogBot = Bot.get().shell("Create New Data Type").bot();
		dialogBot.textWithLabel("Data Type").setText(name);
		dialogBot.button("OK").click();
	}
	
	public String getName() {
		return name;
	}
	
}
