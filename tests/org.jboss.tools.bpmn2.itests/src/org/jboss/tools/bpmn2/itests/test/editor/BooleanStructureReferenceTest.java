package org.jboss.tools.bpmn2.itests.test.editor;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.constructs.JBPM5Process;
import org.jboss.tools.bpmn2.itests.editor.constructs.eventdefinitions.TerminateEventDefinition;
import org.jboss.tools.bpmn2.itests.editor.constructs.events.EndEvent;
import org.jboss.tools.bpmn2.itests.editor.constructs.events.StartEvent;
import org.jboss.tools.bpmn2.itests.editor.constructs.tasks.ScriptTask;
import org.jboss.tools.bpmn2.itests.editor.constructs.tasks.UserTask;
import org.jboss.tools.bpmn2.itests.editor.properties.variables.JBPM5OutputParameter;
import org.jboss.tools.bpmn2.itests.editor.properties.variables.OutputParameter;
import org.jboss.tools.bpmn2.itests.editor.properties.variables.ParameterVariableMapping;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;

import org.junit.Test;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-BooleanStructureRef", project="EditorTestProject")
public class BooleanStructureReferenceTest extends JBPM6BaseTest {

	@Test
	public void runTest() throws Exception {
		JBPM5Process process = new JBPM5Process("BPMN2-BooleanStructureRef");
		process.addDataType("Boolean");
		process.addLocalVariable("test", "Boolean");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("User Task", ConstructType.USER_TASK);

		UserTask userTask = new UserTask("User Task");
		userTask.addParameterMapping(new JBPM5OutputParameter("testHT", null, new ParameterVariableMapping("StructureRef/test")));
		userTask.append("Script", ConstructType.SCRIPT_TASK);

		ScriptTask scriptTask = new ScriptTask("Script");
		scriptTask.setScript("", "System.out.println(\"Result \" + test)");
		scriptTask.append("EndProcess", ConstructType.END_EVENT);
		
		EndEvent end = new EndEvent("EndProcess");
//		end.addEventDefinition(new TerminateEventDefinition()); // BUG!
	}
	
}