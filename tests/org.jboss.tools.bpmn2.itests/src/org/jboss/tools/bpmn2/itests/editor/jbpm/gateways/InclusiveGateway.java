package org.jboss.tools.bpmn2.itests.editor.jbpm.gateways;

import org.jboss.tools.bpmn2.itests.editor.AbstractGateway;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class InclusiveGateway extends AbstractGateway {

	/**
	 * 
	 * @param name
	 */
	public InclusiveGateway(String name) {
		super(name, ConstructType.INCLUSIVE_GATEWAY);
	}
	
	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.AbstractGateway#setDirection(org.jboss.tools.bpmn2.itests.editor.AbstractGateway.Direction)
	 */
	@Override
	public void setDirection(Direction direction) {
		super.setDirection(direction);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.AbstractGateway#setCondition(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void setCondition(String branch, String lang, String condition) {
		super.setCondition(branch, lang, condition);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.AbstractGateway#setDefaultBranch(java.lang.String)
	 */
	@Override
	public void setDefaultBranch(String branch) {
		super.setDefaultBranch(branch);
	}
	
}
