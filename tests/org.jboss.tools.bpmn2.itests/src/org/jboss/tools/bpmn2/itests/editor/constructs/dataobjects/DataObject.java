package org.jboss.tools.bpmn2.itests.editor.constructs.dataobjects;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.constructs.AbstractDataObject;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class DataObject extends AbstractDataObject {

	public DataObject(String name) {
		super(name, ConstructType.DATA_OBJECT);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractDataObject#setDataObjectType(java.lang.String)
	 */
	@Override
	public void setDataObjectType(String dataType) {
		super.setDataObjectType(dataType);
	}
	
}
