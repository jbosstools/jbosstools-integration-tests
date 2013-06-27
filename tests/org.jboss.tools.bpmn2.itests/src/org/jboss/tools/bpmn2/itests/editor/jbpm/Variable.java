package org.jboss.tools.bpmn2.itests.editor.jbpm;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.jboss.tools.bpmn2.itests.editor.IVariable;
import org.jboss.tools.bpmn2.itests.reddeer.BPMN2PropertiesView;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class Variable implements IVariable {

	protected BPMN2PropertiesView properties = new BPMN2PropertiesView();
	
	protected SWTBot bot;
	
	private String sectionName = "Variable List";

	private String name;

	private DataType dataType;
	
	/**
	 * 
	 * @param name
	 */
	public Variable(String name) {
		this(name, null);
	}
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 * @param sectionName
	 */
	public Variable(String name, DataType dataType) {
		this.name = name;
		this.dataType = dataType;
	}
	
	/**
	 * 
	 */
	public void add() {
		int index = properties.indexOfSection(sectionName);
		bot.toolbarButtonWithTooltip("Add", index).click();
		bot.textWithLabel("Name").setText(name);
		if(dataType != null) {
			try {
				bot.comboBoxWithLabel("Data Type").setSelection(dataType.getName());
			} catch (Exception e) {
				dataType.add();
			}
		}
		bot.toolbarButtonWithTooltip("Close").click();
	}

	/**
	 * 
	 */
	public void remove() {
		int index = properties.indexOfSection(sectionName);
		
		bot.table(index).select(name);
		bot.toolbarButtonWithTooltip("Remove", index).click();
	}
}
