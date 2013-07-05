package org.jboss.tools.bpmn2.itests.editor.jbpm.swimlanes;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.ContainerConstruct;


/**
 * 
 * @author mbaluch
 */
public class Lane extends ContainerConstruct {

	public Lane(String name) {
		super(name, ConstructType.LANE);
	}
	
}
