package org.jboss.tools.bpmn2.itests.test.editor;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.jbpm.BPMN2Process;
import org.jboss.tools.bpmn2.itests.editor.jbpm.FromVariable;
import org.jboss.tools.bpmn2.itests.editor.jbpm.InputParameterMapping;
import org.jboss.tools.bpmn2.itests.editor.jbpm.ToDataInput;
import org.jboss.tools.bpmn2.itests.editor.jbpm.activities.SendTask;
import org.jboss.tools.bpmn2.itests.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;
import org.junit.Test;

/**
 * ISSUES:
 * 	1) engine does not validate the presence of the rules.
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-SendTask", project="EditorTestProject")
public class SendTaskTest extends JBPM6BaseTest {

	/**
	 *
	 *  
	 * @throws Exception
	 */
	@Test
	public void runTest() throws Exception {
		BPMN2Process process = new BPMN2Process("BPMN2-SendTask");
		process.addLocalVariable("s", "String");
		process.addMessage("_2_Message", "String");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("Send", ConstructType.SEND_TASK);

		SendTask send = new SendTask("Send");
		send.setImplementation("Other");
		send.setMessage("_2_Message", "String");
		send.addParameterMapping(new InputParameterMapping(new FromVariable("BPMN2-SendTask/s"), new ToDataInput("Message")));
		send.append("EndProcess", ConstructType.TERMINATE_END_EVENT);
	}
	
}