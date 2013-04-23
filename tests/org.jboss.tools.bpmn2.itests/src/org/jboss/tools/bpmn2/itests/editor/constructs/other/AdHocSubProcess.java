package org.jboss.tools.bpmn2.itests.editor.constructs.other;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.constructs.ContainerConstruct;

/**
 * TBD
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class AdHocSubProcess extends ContainerConstruct {

	/**
	 * 
	 * @param name
	 */
	public AdHocSubProcess(String name) {
		super(name, ConstructType.AD_HOC_SUB_PROCESS);
	}
	
}
