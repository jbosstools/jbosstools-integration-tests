package org.jboss.tools.bpmn2.itests.editor.jbpm.gateways;

import org.jboss.reddeer.swt.impl.table.DefaultTable;
import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.itests.editor.AbstractGateway;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;

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
	 * 
	 * @param name
	 * @param priority
	 */
	public void setPriority(String branch, String priority) {
		select();
		properties.selectTab("Gateway");
		new DefaultTable(0).select(branch);
		properties.toolbarButton("Sequence Flow List", "Edit").click();
		new LabeledText("Priority").setText(priority);
		properties.toolbarButton("Sequence Flow Details", "Close").click();
	}
	
	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.AbstractGateway#setDirection(org.jboss.tools.bpmn2.itests.editor.AbstractGateway.Direction)
	 */
	@Override
	public void setDirection(Direction direction) {
		super.setDirection(direction);
	}
	
	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.AbstractGateway#setCondition(java.lang.String, java.lang.String)
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
