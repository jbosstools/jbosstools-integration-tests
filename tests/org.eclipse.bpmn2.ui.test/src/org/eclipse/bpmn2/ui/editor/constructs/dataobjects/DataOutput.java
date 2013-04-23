package org.eclipse.bpmn2.ui.editor.constructs.dataobjects;

import org.eclipse.bpmn2.ui.editor.ConstructType;
import org.eclipse.bpmn2.ui.editor.constructs.AbstractDataObject;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class DataOutput extends AbstractDataObject {

	/**
	 * 
	 * @param name
	 */
	public DataOutput(String name) {
		super(name, ConstructType.DATA_OUTPUT);
	}

	/**
	 * @see org.eclipse.bpmn2.ui.editor.constructs.AbstractDataObject#setDataObjectName(java.lang.String)
	 */
	@Override
	public void setDataObjectName(String name) {
		super.setDataObjectName(name);
	}

	/**
	 * @see org.eclipse.bpmn2.ui.editor.constructs.AbstractDataObject#setDataObjectType(java.lang.String)
	 */
	@Override
	public void setDataObjectType(String dataType) {
		super.setDataObjectType(dataType);
	}
	
}
