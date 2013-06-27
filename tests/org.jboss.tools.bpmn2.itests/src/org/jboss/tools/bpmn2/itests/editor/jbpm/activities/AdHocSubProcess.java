package org.jboss.tools.bpmn2.itests.editor.jbpm.activities;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.ContainerConstruct;

/**
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
