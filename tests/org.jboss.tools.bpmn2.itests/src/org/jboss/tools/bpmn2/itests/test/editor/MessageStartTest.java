package org.jboss.tools.bpmn2.itests.test.editor;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.jbpm.BPMN2Process;
import org.jboss.tools.bpmn2.itests.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.itests.editor.jbpm.startevents.MessageStartEvent;
import org.jboss.tools.bpmn2.itests.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;
import org.junit.Test;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-MessageStart", project="EditorTestProject")
public class MessageStartTest extends JBPM6BaseTest {

	/**
	 *
	 *  
	 * @throws Exception
	 */
	@Test
	public void runTest() throws Exception {
		BPMN2Process process = new BPMN2Process("BPMN2-MessageStart");
		process.addLocalVariable("x", "String");
		process.addMessage("HelloMessage", "String");
		
		new StartEvent("StartProcess").delete();
		process.add("StartProcess", ConstructType.MESSAGE_START_EVENT);
		
		MessageStartEvent start = new MessageStartEvent("StartProcess");
		start.setMessage("HelloMessage", "String");
		start.append("Script", ConstructType.SCRIPT_TASK);
		
		ScriptTask script = new ScriptTask("Script");
		script.setScript("Java", "System.out.println(\"x = \" + x);");
		script.append("EndProcess", ConstructType.TERMINATE_END_EVENT);
	}
	
}