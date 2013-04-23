package org.eclipse.bpmn2.ui.editor.properties.variables;

import org.eclipse.bpmn2.ui.editor.properties.datatypes.AbstractDataState;
import org.eclipse.bpmn2.ui.editor.properties.datatypes.AbstractDataType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class Variable extends AbstractVariable {

	private String name;
	private AbstractDataState dataState;
	private AbstractDataType  dataType;
	
	/**
	 * 
	 * @param name
	 */
	public Variable(String name) {
		this(name, null, null);
	}
	
	/**
	 * 
	 * @param name
	 * @param dataState
	 * @param dataType
	 */
	public Variable(String name, AbstractDataState dataState, AbstractDataType dataType) {
		super(name, "Variable List");
		
		this.dataState = dataState;
		this.dataType  = dataType;
	}
	
	/**
	 * 
	 */
	public void add() {
		int index = properties.indexOfSection(sectionName);
		
		bot.textWithLabel("Name").setText(name);
		bot.toolbarButtonWithTooltip("Add", index).click();
		
		try {
			bot.comboBoxWithLabel("Data State").setSelection(dataState.getStateName());
		} catch (Exception e) {
			dataState.add();
		}

		try {
			bot.comboBoxWithLabel("Data Type").setSelection(dataType.getTypeName());
		} catch (Exception e) {
			dataType.add();
		}
		bot.toolbarButtonWithTooltip("Close").click();
	}

}
