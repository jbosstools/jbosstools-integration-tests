package org.eclipse.bpmn2.ui.editor.constructs.other;

import org.eclipse.bpmn2.ui.editor.ConstructType;
import org.eclipse.bpmn2.ui.editor.constructs.ContainerConstruct;

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
