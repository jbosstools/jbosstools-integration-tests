package org.jboss.tools.bpmn2.itests.test.editor;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.jbpm.activities.Task;
import org.jboss.tools.bpmn2.itests.editor.jbpm.endevents.EndEvent;
import org.jboss.tools.bpmn2.itests.editor.jbpm.endevents.TerminateEndEvent;
import org.jboss.tools.bpmn2.itests.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.itests.editor.jbpm.swimlanes.Lane;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;
import org.junit.Test;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-Lane", project="EditorTestProject")
public class LaneTest extends JBPM6BaseTest {

	/**
	 *
	 *  
	 * @throws Exception
	 */
	@Test
	public void runTest() throws Exception {
		StartEvent start = new StartEvent("StartProcess");
		start.append("MyLane", ConstructType.LANE);
		
		Lane lane = new Lane("MyLane");
		lane.append("EndProcess", ConstructType.TERMINATE_END_EVENT);
		lane.add("Hello", ConstructType.TASK);

		Task task = new Task("Hello");
		task.append("Goodbye", ConstructType.TASK);
		
		Task task2 = new Task("Goodbye");
		
		EndEvent end = new TerminateEndEvent("EndProcess");
		
		start.connectTo(task);
		task2.connectTo(end);
	}
	
}