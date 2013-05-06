package org.jboss.tools.bpmn2.itests.test.editor;

import org.eclipse.swtbot.swt.finder.SWTBotTestCase;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.constructs.JBPM5Process;
import org.jboss.tools.bpmn2.itests.editor.constructs.eventdefinitions.TerminateEventDefinition;
import org.jboss.tools.bpmn2.itests.editor.constructs.events.EndEvent;
import org.jboss.tools.bpmn2.itests.editor.constructs.events.StartEvent;
import org.jboss.tools.bpmn2.itests.editor.constructs.other.CallActivity;
import org.jboss.tools.bpmn2.itests.editor.properties.variables.JBPM5OutputParameter;
import org.jboss.tools.bpmn2.itests.editor.properties.variables.JBPM5Parameter;
import org.jboss.tools.bpmn2.itests.editor.properties.variables.ParameterVariableMapping;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;

import org.junit.Test;

/**
 * ISSUES:
 * 	1) setting target namespace to "" on the process will keep the old
 *     value
 *     
 * @author mbaluch
 *
 */
@ProcessDefinition(name="ParentProcess",  file="BPMN2-CallActivity.bpmn2", project="EditorTestProject")
public class CallActivityTest extends SWTBotTestCase {

	/**
	 *
	 *  
	 * @throws Exception
	 */
	@Test
	public void testCallActivityModeling() throws Exception {
		/*
		 * Modeling
		 */
		
		JBPM5Process process = new JBPM5Process("BPMN2-CallActivity");
//		process.addImport("java.util.List");
//		process.addImport("java.util.ArrayList");
//		process.addDataType("String");
		 
//		process.addDataType("String");
//		process.addLocalVariable("x", "String");
//		process.addLocalVariable("y", "String");
//		process.add("StartProcess", ConstructType.START_EVENT);
//		
		StartEvent start = new StartEvent("StartProcess");
		start.append("CallActivity", ConstructType.CALL_ACTIVITY);
		
		CallActivity call = new CallActivity("CallActivity");
//		call.setWaitForCompletion(true);
//		call.setIndependent(true);
//		call.setCalledActivity("SubProcess");
		call.addParameterMapping(new JBPM5Parameter("subX", null, new ParameterVariableMapping("Evaluation/x")));
		call.addParameterMapping(new JBPM5OutputParameter("subY", null, new ParameterVariableMapping("Evaluation/y")));
		
//		call.append("EndProcess", ConstructType.END_EVENT);
		
		EndEvent end = new EndEvent("EndProcess");
		end.addEventDefinition(new TerminateEventDefinition());
		
		/*
		 * Validate
		 */
		// TBD
	}
	
}