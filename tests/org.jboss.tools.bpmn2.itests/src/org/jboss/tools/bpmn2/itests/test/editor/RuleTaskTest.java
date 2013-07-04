package org.jboss.tools.bpmn2.itests.test.editor;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.jbpm.activities.BusinessRuleTask;
import org.jboss.tools.bpmn2.itests.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;
import org.junit.Test;

/**
 * ISSUES:
 * 	1) engine does not validate the presence of the rules.
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-RuleTask", project="EditorTestProject")
public class RuleTaskTest extends JBPM6BaseTest {

	/**
	 *
	 *  
	 * @throws Exception
	 */
	@Test
	public void runTest() throws Exception {
		StartEvent start = new StartEvent("StartProcess");
		start.append("Rule", ConstructType.BUSINESS_RULE_TASK);
		
		BusinessRuleTask rule = new BusinessRuleTask("Rule");
		rule.setRuleFlowGroup("myRules");
		rule.append("EndProcess", ConstructType.TERMINATE_END_EVENT);
	}
	
}