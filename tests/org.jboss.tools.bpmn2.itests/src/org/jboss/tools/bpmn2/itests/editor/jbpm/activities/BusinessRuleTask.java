package org.jboss.tools.bpmn2.itests.editor.jbpm.activities;

import org.jboss.reddeer.swt.impl.text.LabeledText;
import org.jboss.tools.bpmn2.itests.editor.AbstractTask;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;


/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class BusinessRuleTask extends AbstractTask {
	
	/**
	 * 
	 * @param name
	 */
	public BusinessRuleTask(String name) {
		super(name, ConstructType.BUSINESS_RULE_TASK);
	}

	/**
	 * 
	 * @param group
	 */
	public void setRuleFlowGroup(String group) {
		properties.selectTab("Business Rule Task");
		new LabeledText("Rule Flow Group").setText(group);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.AbstractTask#setIsForCompensation(boolean)
	 */
	@Override
	public void setIsForCompensation(boolean b) {
		super.setIsForCompensation(b);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.AbstractTask#setOnEntryScript(java.lang.String, java.lang.String)
	 */
	@Override
	public void setOnEntryScript(String language, String script) {
		super.setOnEntryScript(language, script);
	}

	/**
	 * @see org.jboss.tools.bpmn2.itests.editor.AbstractTask#setOnExistScript(java.lang.String, java.lang.String)
	 */
	@Override
	public void setOnExistScript(String language, String script) {
		super.setOnExistScript(language, script);
	}
	
}
