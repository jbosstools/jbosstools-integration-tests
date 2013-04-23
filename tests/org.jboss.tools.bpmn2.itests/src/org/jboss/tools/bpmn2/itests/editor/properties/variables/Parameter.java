package org.jboss.tools.bpmn2.itests.editor.properties.variables;

import org.jboss.tools.bpmn2.itests.editor.properties.datatypes.AbstractDataState;
import org.jboss.tools.bpmn2.itests.editor.properties.datatypes.AbstractDataType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class Parameter extends Variable implements IParameter {

	IMapping mapping;
	
	/**
	 * 
	 * @param name
	 */
	public Parameter(String name) {
		this(name, null, null, null);
	}
	
	/**
	 * 
	 * @param name
	 * @param dataState
	 * @param dataType
	 * @param mapping
	 */
	public Parameter(String name, AbstractDataState dataState, AbstractDataType dataType, IMapping mapping) {
		this(name, dataState, dataType, mapping, "Input Parameters");
	}
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 * @param mapping
	 * @param sectionName
	 */
	public Parameter(String name, AbstractDataState dataState, AbstractDataType dataType, IMapping mapping, String sectionName) {
		super(name, dataState, dataType);
		
		this.mapping = mapping;
		
		this.sectionName = sectionName;
	}
	
	/**
	 * 
	 */
	public void add() {
		super.add();
		
		int index = properties.indexOfSection(sectionName);
		
		bot.toolbarButtonWithTooltip("Edit", index).click();
		mapping.add();
		bot.toolbarButtonWithTooltip("Close").click();
	}
	
	/**
	 * 
	 */
	public void remove() {
		int index = properties.indexOfSection(sectionName);
		
		bot.table(1).select(getName());
		bot.toolbarButtonWithTooltip("Remove", index);
	}
	
}
