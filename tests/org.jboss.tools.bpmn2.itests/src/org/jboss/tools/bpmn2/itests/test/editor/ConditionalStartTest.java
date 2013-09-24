package org.jboss.tools.bpmn2.itests.test.editor;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.jbpm.BPMN2Process;
import org.jboss.tools.bpmn2.itests.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.itests.editor.jbpm.startevents.ConditionalStartEvent;
import org.jboss.tools.bpmn2.itests.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;
import org.junit.Test;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-ConditionalStart", project="EditorTestProject")
public class ConditionalStartTest extends JBPM6BaseTest {

	/**
	 *
	 *  
	 * @throws Exception
	 */
	@Test
	public void runTest() throws Exception {
		new StartEvent("StartProcess").delete();
		new BPMN2Process("BPMN2-ConditionalStart").add("StartProcess", ConstructType.CONDITIONAL_START_EVENT);
		
		ConditionalStartEvent startEvent = new ConditionalStartEvent("StartProcess");
		startEvent.setCondition("", "org.jbpm.bpmn2.objects.Person(name == \"john\")");
		startEvent.append("Hello", ConstructType.SCRIPT_TASK);
		
		ScriptTask scriptTask = new ScriptTask("Hello");
		scriptTask.setScript("", "System.out.println(\"Hello World\");");
		scriptTask.append("EndProcess", ConstructType.TERMINATE_END_EVENT);
	}
	
}