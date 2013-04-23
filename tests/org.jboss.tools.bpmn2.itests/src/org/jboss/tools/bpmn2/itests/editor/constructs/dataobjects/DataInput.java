package org.jboss.tools.bpmn2.itests.editor.constructs.dataobjects;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.constructs.AbstractDataObject;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class DataInput extends AbstractDataObject {

	public DataInput(String name) {
		super(name, ConstructType.DATA_INPUT);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractDataObject#setDataObjectName(java.lang.String)
	 */
	@Override
	public void setDataObjectName(String name) {
		super.setDataObjectName(name);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractDataObject#setDataObjectType(java.lang.String)
	 */
	@Override
	public void setDataObjectType(String dataType) {
		super.setDataObjectType(dataType);
	}
	
}
