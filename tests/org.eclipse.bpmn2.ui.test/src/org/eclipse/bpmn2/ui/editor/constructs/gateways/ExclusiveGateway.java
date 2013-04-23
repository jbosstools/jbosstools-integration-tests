package org.eclipse.bpmn2.ui.editor.constructs.gateways;

import org.eclipse.bpmn2.ui.editor.ConstructType;
import org.eclipse.bpmn2.ui.editor.constructs.AbstractGateway;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class ExclusiveGateway extends AbstractGateway {
	
	/**
	 * 
	 * @param name
	 */
	public ExclusiveGateway(String name) {
		super(name, ConstructType.EXCLUSIVE_GATEWAY);
	}

	/**
	 * @see org.eclipse.bpmn2.ui.editor.constructs.AbstractGateway#setDirection(org.eclipse.bpmn2.ui.editor.constructs.AbstractGateway.Direction)
	 */
	@Override
	public void setDirection(Direction direction) {
		super.setDirection(direction);
	}
	
	/**
	 * @see org.eclipse.bpmn2.ui.editor.constructs.AbstractGateway#setCondition(java.lang.String, java.lang.String)
	 */
	@Override
	public void setCondition(String branch, String condition) {
		super.setCondition(branch, condition);
	}

	/**
	 * @see org.eclipse.bpmn2.ui.editor.constructs.AbstractGateway#setDefaultBranch(java.lang.String)
	 */
	@Override
	public void setDefaultBranch(String branch) {
		super.setDefaultBranch(branch);
	}
	
}
