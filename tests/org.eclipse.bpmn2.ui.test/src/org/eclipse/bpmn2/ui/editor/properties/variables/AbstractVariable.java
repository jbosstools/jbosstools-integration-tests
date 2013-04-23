package org.eclipse.bpmn2.ui.editor.properties.variables;

import org.eclipse.bpmn2.ui.editor.properties.BPMN2PropertiesView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.reddeer.swt.util.Bot;

public abstract class AbstractVariable implements IVariable {

	protected BPMN2PropertiesView properties = new BPMN2PropertiesView();
	
	protected SWTBot bot;
	
	protected String sectionName;

	protected String name;
	
	public AbstractVariable(String name) {
		this(name, "Variable List");
	}
	
	public AbstractVariable(String name, String sectionName) {
		this.name = name;
		this.sectionName = sectionName;
		
		this.bot = Bot.get();
	}
	
	public String getName() {
		return name;
	}
	
	public void remove() {
		int index = properties.indexOfSection(sectionName);
		
		bot.table(index).select(name);
		bot.toolbarButtonWithTooltip("Remove", index).click();
	}

}
