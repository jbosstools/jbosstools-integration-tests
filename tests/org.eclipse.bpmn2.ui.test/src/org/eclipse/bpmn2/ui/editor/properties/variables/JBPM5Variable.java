package org.eclipse.bpmn2.ui.editor.properties.variables;

import org.eclipse.bpmn2.ui.editor.properties.datatypes.JBPM5DataType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class JBPM5Variable extends AbstractVariable {

	private String name;
	private JBPM5DataType dataType;
	
	/**
	 * 
	 * @param name
	 */
	public JBPM5Variable(String name) {
		this(name, null);
	}
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 */
	public JBPM5Variable(String name, JBPM5DataType dataType) {
		this(name, dataType, "Variable List");
	}
	
	/**
	 * 
	 * @param name
	 * @param dataType
	 * @param sectionName
	 */
	public JBPM5Variable(String name, JBPM5DataType dataType, String sectionName) {
		super(name, sectionName);

		this.dataType = dataType;
	}
	
	/**
	 * 
	 */
	public void add() {
		int index = properties.indexOfSection(sectionName);
		
		bot.textWithLabel("Name").setText(name);
		bot.toolbarButtonWithTooltip("Add", index);
		try {
			bot.comboBoxWithLabel("Data Type").setSelection(dataType.getTypeName());
		} catch (Exception e) {
			dataType.add();
		}
		bot.toolbarButtonWithTooltip("Close");
	}

}
