package org.eclipse.bpmn2.ui.editor.constructs.dataobjects;

import org.eclipse.bpmn2.ui.editor.ConstructType;
import org.eclipse.bpmn2.ui.editor.constructs.AbstractDataObject;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class Message extends AbstractDataObject {

	public Message(String name) {
		super(name, ConstructType.MESSAGE);
	}
	
	/**
	 * @see org.eclipse.bpmn2.ui.editor.constructs.AbstractDataObject#setDataObjectType(java.lang.String)
	 */
	@Override
	public void setDataObjectType(String dataType) {
		super.setDataObjectType(dataType);
	}

}
