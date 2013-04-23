package org.eclipse.bpmn2.ui.editor.properties.variables;

import org.eclipse.bpmn2.ui.editor.properties.datatypes.JBPM5DataType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class JBPM5Parameter extends JBPM5Variable implements IParameter {

	private IMapping mapping;
	
	/**
	 * 
	 * @param name
	 */
	public JBPM5Parameter(String name) {
		this(name, null, null);
	}
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 * @param mapping
	 */
	public JBPM5Parameter(String name, JBPM5DataType dataType, IMapping mapping) {
		this(name, dataType, mapping, "Input Parameters");
	}
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 * @param mapping
	 * @param categoryName
	 */
	public JBPM5Parameter(String name, JBPM5DataType dataType, IMapping mapping, String sectionName) {
		super(name, dataType, sectionName);
	}
	
	/**
	 * 
	 */
	public void add() {
		super.add();
		
		int index = properties.indexOfSection(sectionName);
		
		bot.toolbarButtonWithTooltip("Edit", index);
		mapping.add();
		bot.toolbarButtonWithTooltip("Close");
	}
	
}
