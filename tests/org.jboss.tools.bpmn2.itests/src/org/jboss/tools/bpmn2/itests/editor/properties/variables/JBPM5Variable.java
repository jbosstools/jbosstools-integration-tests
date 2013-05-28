package org.jboss.tools.bpmn2.itests.editor.properties.variables;

import org.jboss.tools.bpmn2.itests.editor.properties.datatypes.JBPM5DataType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class JBPM5Variable extends AbstractVariable {

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

		bot.toolbarButtonWithTooltip("Add", index).click();
		bot.textWithLabel("Name").setText(name);
		if(dataType != null) {
			try {
				bot.comboBoxWithLabel("Data Type").setSelection(dataType.getTypeName());
			} catch (Exception e) {
				dataType.add();
			}
		}
		bot.toolbarButtonWithTooltip("Close").click();
	}

}
