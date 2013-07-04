package org.jboss.tools.bpmn2.itests.test.editor;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.itests.editor.jbpm.catchevents.TimerIntermediateCatchEvent;
import org.jboss.tools.bpmn2.itests.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;
import org.junit.Test;

/**
 * ISSUES:
 * 	1) Invalid timer string is not found! E.g. 500###ms
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-IntermediateCatchEventTimerCycle", project="EditorTestProject")
public class IntermediateCatchEventTimerCycleTest extends JBPM6BaseTest {

	/**
	 *
	 *  
	 * @throws Exception
	 */
	@Test
	public void runTest() throws Exception {
		StartEvent startEvent = new StartEvent("StartProcess");
		startEvent.append("Timer", ConstructType.TIMER_INTERMEDIATE_CATCH_EVENT);
		
		TimerIntermediateCatchEvent catchEvent = new TimerIntermediateCatchEvent("Timer");
		catchEvent.setTimer("500ms");
		catchEvent.append("Event", ConstructType.SCRIPT_TASK);
		
		ScriptTask scriptTask = new ScriptTask("Event");
		scriptTask.setScript("Java", "System.out.println(\"Timer triggered\");");
		scriptTask.append("EndProcess", ConstructType.TERMINATE_END_EVENT);
	}
	
}