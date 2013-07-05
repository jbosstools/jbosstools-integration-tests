package org.jboss.tools.bpmn2.itests.test.editor;

import org.jboss.tools.bpmn2.itests.editor.AbstractGateway.Direction;
import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.Position;
import org.jboss.tools.bpmn2.itests.editor.jbpm.BPMN2Process;
import org.jboss.tools.bpmn2.itests.editor.jbpm.FromExpression;
import org.jboss.tools.bpmn2.itests.editor.jbpm.InputParameterMapping;
import org.jboss.tools.bpmn2.itests.editor.jbpm.ToDataInput;
import org.jboss.tools.bpmn2.itests.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.itests.editor.jbpm.activities.Task;
import org.jboss.tools.bpmn2.itests.editor.jbpm.gateways.ExclusiveGateway;
import org.jboss.tools.bpmn2.itests.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;
import org.junit.Test;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-ExclusiveSplitPriority", project="EditorTestProject")
public class ExclusiveSplitPriorityTest extends JBPM6BaseTest {

	/**
	 *
	 *  
	 * @throws Exception
	 */
	@Test
	public void runTest() throws Exception {
		BPMN2Process process = new BPMN2Process("BPMN2-ExclusiveSplitPriority");
		process.addLocalVariable("x", "String");
		process.addLocalVariable("y", "String");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("Split", ConstructType.EXCLUSIVE_GATEWAY);
		
		ExclusiveGateway gw = new ExclusiveGateway("Split");
		gw.setDirection(Direction.DIVERGING);
		gw.append("Script1", ConstructType.SCRIPT_TASK, Position.NORTH_EAST);
		gw.append("Script2", ConstructType.SCRIPT_TASK, Position.SOUTH_EAST);
		gw.setCondition("Split -> Script1", "java", "return x!=null");
		gw.setCondition("Split -> Script2", "java", "return x!=null");
		gw.setPriority("Split -> Script2", "1");
		gw.setPriority("Split -> Script1", "2");
		
		ScriptTask task1 = new ScriptTask("Script1");
		task1.setScript("Java", "System.out.println(\"x=\" + x);");
		task1.append("Join", ConstructType.EXCLUSIVE_GATEWAY, Position.SOUTH_EAST);
		
		ExclusiveGateway gw2 = new ExclusiveGateway("Join");
		gw2.setDirection(Direction.CONVERGING);
		gw2.append("Email", ConstructType.TASK);
		
		ScriptTask task2 = new ScriptTask("Script2");
		task2.setScript("Java", "System.out.println(\"y=\" + y);");
		task2.connectTo(gw2);
		
		Task task = new Task("Email");
		task.addParameterMapping(new InputParameterMapping(new FromExpression("mvel", "This is an urgent email #{x}"), new ToDataInput("Body")));
		task.addParameterMapping(new InputParameterMapping(new FromExpression("mvel", "Urgent email !"), new ToDataInput("Subject")));
		task.addParameterMapping(new InputParameterMapping(new FromExpression("mvel", "you@mail.com"), new ToDataInput("To")));
		task.addParameterMapping(new InputParameterMapping(new FromExpression("mvel", "ne@mail.com"), new ToDataInput("From")));
		task.append("EndProcess", ConstructType.TERMINATE_END_EVENT);
	}
	
}