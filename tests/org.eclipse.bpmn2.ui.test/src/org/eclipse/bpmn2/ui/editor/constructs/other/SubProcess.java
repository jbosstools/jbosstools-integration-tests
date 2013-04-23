package org.eclipse.bpmn2.ui.editor.constructs.other;

import org.eclipse.bpmn2.ui.editor.ConstructType;
import org.eclipse.bpmn2.ui.editor.constructs.Construct;

/**
 * TBD
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class SubProcess extends Construct {

	/**
	 * 
	 * @param name
	 */
	public SubProcess(String name) {
		super(name, ConstructType.SUB_PROCESS_TASK);
	}
	
}
