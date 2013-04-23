package org.eclipse.bpmn2.ui.editor.constructs.gateways;

import org.eclipse.bpmn2.ui.editor.ConstructType;
import org.eclipse.bpmn2.ui.editor.constructs.AbstractGateway;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class ParallelGateway extends AbstractGateway {

	/**
	 * 
	 * @param name
	 */
	public ParallelGateway(String name) {
		super(name, ConstructType.PARALLEL_GATEWAY);
	}
	
	/**
	 * @see org.eclipse.bpmn2.ui.editor.constructs.AbstractGateway#setDirection(org.eclipse.bpmn2.ui.editor.constructs.AbstractGateway.Direction)
	 */
	@Override
	public void setDirection(Direction direction) {
		super.setDirection(direction);
	}
	
}
