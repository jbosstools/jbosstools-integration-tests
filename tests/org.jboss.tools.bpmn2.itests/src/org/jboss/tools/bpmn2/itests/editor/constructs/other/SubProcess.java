package org.jboss.tools.bpmn2.itests.editor.constructs.other;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.constructs.ContainerConstruct;

/**
 * TBD
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class SubProcess extends ContainerConstruct {

	/**
	 * 
	 * @param name
	 */
	public SubProcess(String name) {
		super(name, ConstructType.SUB_PROCESS_TASK);
	}
	
}
