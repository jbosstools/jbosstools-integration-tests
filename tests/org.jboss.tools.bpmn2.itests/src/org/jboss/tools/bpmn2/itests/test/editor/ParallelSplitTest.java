package org.jboss.tools.bpmn2.itests.test.editor;

import org.jboss.tools.bpmn2.itests.editor.AbstractGateway.Direction;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.Position;
import org.jboss.tools.bpmn2.itests.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.itests.editor.jbpm.gateways.ParallelGateway;
import org.jboss.tools.bpmn2.itests.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;
import org.junit.Test;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-ParallelSplit", project="EditorTestProject")
public class ParallelSplitTest extends JBPM6BaseTest {

	/**
	 *
	 *  
	 * @throws Exception
	 */
	@Test
	public void runTest() throws Exception {
		StartEvent start = new StartEvent("StartProcess");
		start.append("ParallelSplit", ConstructType.PARALLEL_GATEWAY);
		
		ParallelGateway gateway = new ParallelGateway("ParallelSplit");
		gateway.setDirection(Direction.DIVERGING);
		gateway.append("Script1", ConstructType.SCRIPT_TASK, Position.NORTH_EAST);
		gateway.append("Script2", ConstructType.SCRIPT_TASK, Position.SOUTH_EAST);
		
		ScriptTask script1 = new ScriptTask("Script1");
		script1.setScript("Java", "System.out.println(\"1\");");
		script1.append("End1", ConstructType.END_EVENT);
		
		// Fails on setScript and I don't know why!
		ScriptTask script2 = new ScriptTask("Script2");
		script2.select();
		script2.setScript("Java", "System.out.println(\"2\");");
		script2.append("End2", ConstructType.END_EVENT);
	}
	
}