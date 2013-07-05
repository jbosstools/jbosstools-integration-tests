package org.jboss.tools.bpmn2.itests.test.editor;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.jbpm.BPMN2Process;
import org.jboss.tools.bpmn2.itests.editor.jbpm.activities.ReceiveTask;
import org.jboss.tools.bpmn2.itests.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;
import org.junit.Test;

/**
 * ISSUE: Looks like this test creates on itemDefinition plus (should be 1 but there are 2)
 *     <itemDefinition .../>
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-ReceiveTask", project="EditorTestProject")
public class ReceiveTaskTest extends JBPM6BaseTest {

	/**
	 *
	 *  
	 * @throws Exception
	 */
	@Test
	public void runTest() throws Exception {
		BPMN2Process process = new BPMN2Process("BPMN2-ReceiveTask");
		process.addLocalVariable("s", "String");
		process.addMessage("HelloMessage", "String");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("Receive", ConstructType.RECEIVE_TASK);
		
		ReceiveTask receive = new ReceiveTask("Receive");
		receive.setImplementation("Other");
		receive.setMessage("HelloMessage", "String");
		receive.append("EndProcess", ConstructType.TERMINATE_END_EVENT);
	}
	
}