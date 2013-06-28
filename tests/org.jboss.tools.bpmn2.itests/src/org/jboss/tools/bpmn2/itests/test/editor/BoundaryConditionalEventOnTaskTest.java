package org.jboss.tools.bpmn2.itests.test.editor;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.Position;
import org.jboss.tools.bpmn2.itests.editor.jbpm.BPMN2Process;
import org.jboss.tools.bpmn2.itests.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.itests.editor.jbpm.activities.UserTask;
import org.jboss.tools.bpmn2.itests.editor.jbpm.boundaryevents.ConditionalBoundaryEvent;
import org.jboss.tools.bpmn2.itests.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;
import org.junit.Test;

/**
 * ISSUE: language should be 'http://www.jboss.org/drools/rule' but it's not available.
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-BoundaryConditionalEventOnTask", project="EditorTestProject")
public class BoundaryConditionalEventOnTaskTest extends JBPM6BaseTest {

	@Test
	public void runTest() throws Exception {
		BPMN2Process process = new BPMN2Process("BPMN2-BoundaryConditionalEventOnTask");
		process.addDataType("String");
		process.addLocalVariable("x", "String");
		
		StartEvent startEvent = new StartEvent("StartProcess");
		startEvent.append("User Task", ConstructType.USER_TASK);
		
		UserTask userTask1 = new UserTask("User Task");
		userTask1.addActor("", "john");
		userTask1.append("User Task 2", ConstructType.USER_TASK, Position.NORTH_EAST);
		userTask1.addEvent("Conditional Boundary Event", ConstructType.CONDITIONAL_BOUNDARY_EVENT);

		// ISSUE: language should be 'http://www.jboss.org/drools/rule' but it's not available.
		ConditionalBoundaryEvent boundaryEvent = new ConditionalBoundaryEvent("Conditional Boundary Event");
		boundaryEvent.setScript("", "org.jbpm.bpmn2.objects.Person(name == \"john\")");
		
		boundaryEvent.append("Condition met", ConstructType.SCRIPT_TASK, Position.SOUTH_EAST);
		UserTask userTask2 = new UserTask("User Task 2");
		userTask2.addActor("", "john");
		userTask2.append("End 1", ConstructType.END_EVENT);
		
		ScriptTask scriptTask1 = new ScriptTask("Condition met");
		scriptTask1.setScript("", "System.out.println(\"Conditional boundary event executed\";)");
		scriptTask1.append("End 2", ConstructType.END_EVENT);
	}
	
}