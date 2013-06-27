package org.jboss.tools.bpmn2.itests.test.editor;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.jbpm.activities.BusinessRuleTask;
import org.jboss.tools.bpmn2.itests.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;
import org.junit.Test;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-BusinessRuleTask", project="EditorTestProject")
public class BusinessRuleTaskTest extends JBPM6BaseTest {

	@Test
	public void runTest() throws Exception {
		StartEvent start = new StartEvent("StartProcess");
		start.append("Business Rule Task", ConstructType.BUSINESS_RULE_TASK);
		
		BusinessRuleTask task = new BusinessRuleTask("Business Rule Task");
		task.setRuleFlowGroup("MyRuleFlowGroup");
		task.append("EndProcess", ConstructType.END_EVENT);
	}
	
}