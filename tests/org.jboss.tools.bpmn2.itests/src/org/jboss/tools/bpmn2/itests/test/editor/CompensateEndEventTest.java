package org.jboss.tools.bpmn2.itests.test.editor;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.constructs.JBPM5Process;
import org.jboss.tools.bpmn2.itests.editor.constructs.eventdefinitions.CompensateEventDefinition;
import org.jboss.tools.bpmn2.itests.editor.constructs.events.EndEvent;
import org.jboss.tools.bpmn2.itests.editor.constructs.events.StartEvent;
import org.jboss.tools.bpmn2.itests.editor.constructs.tasks.ScriptTask;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;
import org.junit.Ignore;
import org.junit.Test;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-CompensateEndEvent", project="EditorTestProject")
public class CompensateEndEventTest extends JBPM6BaseTest {

	@Test
	public void runTest() throws Exception {
		JBPM5Process process = new JBPM5Process("BPMN2-CompensateEndEvent");
		
		StartEvent startEvent = new StartEvent("StartProcess");
		startEvent.append("Task", ConstructType.SCRIPT_TASK);
		
		ScriptTask scriptTask = new ScriptTask("Task");
		scriptTask.setScript("", "System.out.println(\"Executing task\");");
		scriptTask.append("CompensateEvent", ConstructType.END_EVENT);

		EndEvent endEvent = new EndEvent("CompensateEvent");
		/*
		 * Will not have any effect. The second attribute is not present in jBPM.
		 */
		endEvent.addEventDefinition(new CompensateEventDefinition("Task", false));
		
		// TODO: Add boundary event. NOT IN PALETTE. 
	}
	
}