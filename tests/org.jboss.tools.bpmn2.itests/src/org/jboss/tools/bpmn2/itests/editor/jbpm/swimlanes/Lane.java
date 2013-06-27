package org.jboss.tools.bpmn2.itests.editor.jbpm.swimlanes;

import org.jboss.tools.bpmn2.itests.editor.Construct;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;

public class Lane extends Construct {

	public Lane(String name) {
		super(name, ConstructType.LANE);
	}
	
}
