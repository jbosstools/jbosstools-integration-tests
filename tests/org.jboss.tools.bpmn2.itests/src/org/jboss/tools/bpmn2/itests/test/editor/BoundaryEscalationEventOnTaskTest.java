package org.jboss.tools.bpmn2.itests.test.editor;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.Position;
import org.jboss.tools.bpmn2.itests.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.itests.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.itests.editor.jbpm.boundaryevents.EscalationBoundaryEvent;
import org.jboss.tools.bpmn2.itests.editor.jbpm.endevents.EscalationEndEvent;
import org.jboss.tools.bpmn2.itests.editor.jbpm.gateways.ParallelGateway;
import org.jboss.tools.bpmn2.itests.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;
import org.junit.Test;

/**
 * ISSUE: language should be 'http://www.jboss.org/drools/rule' but it's not available.
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-BoundaryEscalationEventOnTask", project="EditorTestProject")
public class BoundaryEscalationEventOnTaskTest extends JBPM6BaseTest {

	@Test
	public void runTest() throws Exception {
		StartEvent startEvent = new StartEvent("StartProcess");
		startEvent.append("Split", ConstructType.PARALLEL_GATEWAY);
		
		ParallelGateway gateway = new ParallelGateway("Split");
		gateway.append("User Task With Escalation", ConstructType.USER_TASK, Position.NORTH_EAST);
		gateway.append("User Task", ConstructType.USER_TASK, Position.SOUTH_EAST);

		UserTask userTask1 = new UserTask("User Task With Escalation");
		userTask1.addActor("", "John");
		userTask1.append("EscalationEndProcess", ConstructType.ESCALATION_END_EVENT);

		EscalationEndEvent escalationEndEvent = new EscalationEndEvent("EscalationEndProcess");
		escalationEndEvent.setEscalation("","MyEscalation");
	
		UserTask userTask2 = new UserTask("User Task");
		userTask2.addActor("", "Mary");
		userTask2.append("EndProcess", ConstructType.END_EVENT);
		userTask2.addEvent("Escalation Boundary Event", ConstructType.ESCALATION_BOUNDARY_EVENT);

		EscalationBoundaryEvent boundaryEvent = new EscalationBoundaryEvent("Escalation Boundary Event");
		boundaryEvent.setEscalation("","MyEscalation");
		boundaryEvent.append("Script Task", ConstructType.SCRIPT_TASK, Position.SOUTH);
		
		ScriptTask scriptTask = new ScriptTask("Script Task");
		scriptTask.setScript("Java", "System.out.println(\"Escalation handled\");");
		scriptTask.append("EndProcess2", ConstructType.END_EVENT);
	}
	
}