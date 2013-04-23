package org.eclipse.bpmn2.ui.editor.constructs.dataobjects;

import org.eclipse.bpmn2.ui.editor.ConstructType;
import org.eclipse.bpmn2.ui.editor.constructs.AbstractDataObject;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class DataObject extends AbstractDataObject {

	public DataObject(String name) {
		super(name, ConstructType.DATA_OBJECT);
	}

	/**
	 * @see org.eclipse.bpmn2.ui.editor.constructs.AbstractDataObject#setDataObjectType(java.lang.String)
	 */
	@Override
	public void setDataObjectType(String dataType) {
		super.setDataObjectType(dataType);
	}
	
}
