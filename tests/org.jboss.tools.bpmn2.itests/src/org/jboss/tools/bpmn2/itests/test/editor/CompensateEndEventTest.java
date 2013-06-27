package org.jboss.tools.bpmn2.itests.test.editor;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.jbpm.BPMN2Process;
import org.jboss.tools.bpmn2.itests.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.itests.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;
import org.junit.Test;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-CompensateEndEvent", project="EditorTestProject")
public class CompensateEndEventTest extends JBPM6BaseTest {

	@Test
	public void runTest() throws Exception {
		BPMN2Process process = new BPMN2Process("BPMN2-CompensateEndEvent");
		
		StartEvent startEvent = new StartEvent("StartProcess");
		startEvent.append("Task", ConstructType.SCRIPT_TASK);
		
		ScriptTask scriptTask = new ScriptTask("Task");
		scriptTask.setScript("", "System.out.println(\"Executing task\");");
		scriptTask.append("CompensateEvent", ConstructType.COMPENSATION_END_EVENT);

		// TODO: Add boundary event. NOT IN PALETTE. 
	}
	
}