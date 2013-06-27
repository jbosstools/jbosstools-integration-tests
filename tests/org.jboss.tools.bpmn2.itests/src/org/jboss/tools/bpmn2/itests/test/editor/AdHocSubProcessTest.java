package org.jboss.tools.bpmn2.itests.test.editor;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.Position;
import org.jboss.tools.bpmn2.itests.editor.jbpm.activities.AdHocSubProcess;
import org.jboss.tools.bpmn2.itests.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.itests.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;
import org.junit.Test;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-AdHocSubProcess",  project="EditorTestProject")
public class AdHocSubProcessTest extends JBPM6BaseTest {

	/**
	 * ISSUES: - May contain another bug. When adding a connection from an element
	 *           to itself then 'y' is missing in the 'di' element.
	 *         - See ContainerConstruct.add(String, ConstructType) 
	 *         
	 */
	@Test
	public void runTest() {
		StartEvent start = new StartEvent("StartProcess");
		start.append("Hello", ConstructType.AD_HOC_SUB_PROCESS);

		AdHocSubProcess subprocess = new AdHocSubProcess("Hello");
		subprocess.append("Goodbye", ConstructType.SCRIPT_TASK);		

		ScriptTask task3 = new ScriptTask("Goodbye");
		task3.setScript("", "System.out.println(\"Goodbye World\");");
		task3.append("EndProcess", ConstructType.TERMINATE_END_EVENT);
		
		/*
		 * Finish ad-hoc sub-process.
		 */
		subprocess.add("Hello1", ConstructType.SCRIPT_TASK);
		
		ScriptTask task1 = new ScriptTask("Hello1");
		task1.setScript("", "System.out.println(\"Hello World 1\");");
		
		subprocess.add("Hello2", ConstructType.SCRIPT_TASK, task1, Position.SOUTH);
		
		ScriptTask task2 = new ScriptTask("Hello2");
		task2.setScript("", "System.out.println(\"Hello World 2\");");
		task2.append("Hello", ConstructType.USER_TASK);
	}
	
}