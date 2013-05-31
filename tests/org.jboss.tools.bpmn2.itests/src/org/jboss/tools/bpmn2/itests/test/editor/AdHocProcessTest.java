package org.jboss.tools.bpmn2.itests.test.editor;

import org.jboss.tools.bpmn2.itests.editor.ConnectionType;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.Position;
import org.jboss.tools.bpmn2.itests.editor.constructs.JBPM5Process;
import org.jboss.tools.bpmn2.itests.editor.constructs.eventdefinitions.TerminateEventDefinition;
import org.jboss.tools.bpmn2.itests.editor.constructs.events.EndEvent;
import org.jboss.tools.bpmn2.itests.editor.constructs.events.StartEvent;
import org.jboss.tools.bpmn2.itests.editor.constructs.gateways.ExclusiveGateway;
import org.jboss.tools.bpmn2.itests.editor.constructs.tasks.ScriptTask;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;

import org.junit.Test;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-AdHocProcess",  project="EditorTestProject")
public class AdHocProcessTest extends JBPM6BaseTest {

	/**
	 *  
	 * @throws Exception
	 */
	@Test
	public void runTest() throws Exception {
		/*
		 * Modeling
		 */
		JBPM5Process process = new JBPM5Process("BPMN2-AdHocProcess");
		process.setAddHoc(true);
		process.add("Task 3", ConstructType.SCRIPT_TASK);

		new StartEvent("StartProcess").delete();
		
		ScriptTask task3 = new ScriptTask("Task 3");
		/*
		 * ISSUE: Empty values can be set only at the beginning!
		 */
		task3.setScript("", "System.out.println(\"Task3\");");
		task3.append("Gateway", ConstructType.EXCLUSIVE_GATEWAY);
		
		ExclusiveGateway gateway = new ExclusiveGateway("Gateway");
		gateway.append("End", ConstructType.END_EVENT, ConnectionType.SEQUENCE_FLOW, Position.NORTH_EAST);
		gateway.append("Task 4", ConstructType.SCRIPT_TASK, ConnectionType.SEQUENCE_FLOW, Position.SOUTH_EAST);
		gateway.setCondition("Gateway -> End", "Rule", "org.jbpm.examples.junit.Person()");
		gateway.setCondition("Gateway -> Task 4", "Rule", "not org.jbpm.examples.junit.Person()");
		
		ScriptTask task4 = new ScriptTask("Task 4");
		task4.setScript("", "System.out.println(\"Task4\");");

		EndEvent end = new EndEvent("End");
//		end.addEventDefinition(new TerminateEventDefinition());
		
		/*
		 * Finish parallel activities
		 */
		process.add("Task 2", ConstructType.SCRIPT_TASK, task3, Position.NORTH);
		
		ScriptTask task2 = new ScriptTask("Task 2");
		task2.setScript("", "System.out.println(\"Task2\");");
		
		process.add("Task 1", ConstructType.SCRIPT_TASK, task2, Position.NORTH);
		
		ScriptTask task1 = new ScriptTask("Task 1");
		task1.setScript("", "System.out.println(\"Task1\");");
		
		process.add("User", ConstructType.USER_TASK, task3, Position.SOUTH);
		
		/*
		 * Validate
		 */
		process.getEditor().save();
	}
	
}