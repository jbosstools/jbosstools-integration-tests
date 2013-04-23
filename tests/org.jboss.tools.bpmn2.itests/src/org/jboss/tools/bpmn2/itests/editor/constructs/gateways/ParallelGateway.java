package org.jboss.tools.bpmn2.itests.editor.constructs.gateways;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.constructs.AbstractGateway;

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
	 * @see org.jboss.tools.bpmn2.itests.editor.constructs.AbstractGateway#setDirection(org.jboss.tools.bpmn2.itests.editor.constructs.AbstractGateway.Direction)
	 */
	@Override
	public void setDirection(Direction direction) {
		super.setDirection(direction);
	}
	
}
