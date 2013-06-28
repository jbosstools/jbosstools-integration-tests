package org.jboss.tools.bpmn2.itests.editor.jbpm.boundaryevents;

import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.tools.bpmn2.itests.editor.Construct;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;

/**
 * 
 * @author mbaluch
 */
public class BoundaryEvent extends Construct {

	public BoundaryEvent(String name) {
		super(name, ConstructType.BOUNDARY_EVENT);
	}
	
	BoundaryEvent(String name, ConstructType type) {
		super(name, type);
	}
	
	public void setCancelActivity(boolean b) {
		properties.selectTab("Event");
		properties.selectCheckBox(new CheckBox("Cancel Activity"), b);
	}
	
}
