package org.jboss.tools.bpmn2.itests.test.editor;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.Position;
import org.jboss.tools.bpmn2.itests.editor.constructs.JBPM5Process;
import org.jboss.tools.bpmn2.itests.editor.constructs.events.StartEvent;
import org.jboss.tools.bpmn2.itests.editor.constructs.tasks.ScriptTask;
import org.jboss.tools.bpmn2.itests.editor.constructs.tasks.UserTask;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;
import org.junit.Ignore;
import org.junit.Test;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-BoundaryConditionalEventOnTask", project="EditorTestProject")
public class BoundaryConditionalEventOnTaskTest extends JBPM6BaseTest {

	/**
	 * Opened issue: https://bugs.eclipse.org/bugs/show_bug.cgi?id=409841
	 */
	@Ignore
	@Test
	public void runTest() throws Exception {
		JBPM5Process process = new JBPM5Process("BPMN2-BoundaryConditionalEventOnTask");
		process.addDataType("String");
		process.addLocalVariable("x", "String");
		
		StartEvent startEvent = new StartEvent("StartProcess");
		startEvent.append("User Task", ConstructType.USER_TASK);
		
		UserTask userTask1 = new UserTask("User Task");
		userTask1.addActor("", "john");
		userTask1.append("User Task 2", ConstructType.USER_TASK, Position.NORTH_EAST);
		userTask1.append("Condition met", ConstructType.SCRIPT_TASK, Position.SOUTH_EAST);
		// TBD: Add boundary event to userTask1. Cannot do this. Is this a bug?
		// 		Is this supported?
		
		UserTask userTask2 = new UserTask("User Task 2");
		userTask2.addActor("", "john");
		userTask2.append("End 1", ConstructType.END_EVENT);
		
		ScriptTask scriptTask1 = new ScriptTask("Condition met");
		scriptTask1.setScript("", "System.out.println(\"Conditional boundary event executed\";)");
		scriptTask1.append("End 2", ConstructType.END_EVENT);
	}
	
}