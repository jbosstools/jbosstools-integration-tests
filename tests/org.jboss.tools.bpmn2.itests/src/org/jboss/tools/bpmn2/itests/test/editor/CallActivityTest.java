package org.jboss.tools.bpmn2.itests.test.editor;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.jbpm.BPMN2Process;
import org.jboss.tools.bpmn2.itests.editor.jbpm.FromDataOutput;
import org.jboss.tools.bpmn2.itests.editor.jbpm.FromVariable;
import org.jboss.tools.bpmn2.itests.editor.jbpm.InputParameterMapping;
import org.jboss.tools.bpmn2.itests.editor.jbpm.OutputParameterMapping;
import org.jboss.tools.bpmn2.itests.editor.jbpm.ToDataInput;
import org.jboss.tools.bpmn2.itests.editor.jbpm.ToVariable;
import org.jboss.tools.bpmn2.itests.editor.jbpm.activities.CallActivity;
import org.jboss.tools.bpmn2.itests.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;
import org.junit.Test;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-CallActivity", project="EditorTestProject")
public class CallActivityTest extends JBPM6BaseTest {

	/**
	 *
	 *  
	 * @throws Exception
	 */
	@Test
	public void runTest() throws Exception {
		BPMN2Process process = new BPMN2Process("BPMN2-CallActivity");
		process.addDataType("String");
		process.addLocalVariable("x", "String");
		process.addLocalVariable("y", "String");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("CallActivity", ConstructType.CALL_ACTIVITY);
		
		CallActivity call = new CallActivity("CallActivity");
		call.setWaitForCompletion(true);
		call.setIndependent(true);
		call.setCalledActivity("SubProcess");
		call.addParameterMapping(new InputParameterMapping(new FromVariable("BPMN2-CallActivity/y"), new ToDataInput("subX")));
		call.addParameterMapping(new OutputParameterMapping(new FromDataOutput("subY"), new ToVariable("BPMN2-CallActivity/x")));
		
		call.append("EndProcess", ConstructType.TERMINATE_END_EVENT);
	}
	
}