package org.eclipse.bpmn2.ui.editor.properties.datatypes;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public abstract class AbstractDataType implements IDataType {

	protected String typeName;
	
	/**
	 * 
	 * @param typeName
	 */
	public AbstractDataType(String typeName) {
		this.typeName = typeName;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTypeName() {
		return typeName;
	}
	
}
