package org.jboss.tools.bpmn2.itests.test.editor;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.Position;
import org.jboss.tools.bpmn2.itests.editor.jbpm.BPMN2Process;
import org.jboss.tools.bpmn2.itests.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.itests.editor.jbpm.gateways.InclusiveGateway;
import org.jboss.tools.bpmn2.itests.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;
import org.junit.Test;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-InclusiveSplit", project="EditorTestProject")
public class InclusiveSplitTest extends JBPM6BaseTest {

	/**
	 *
	 *  
	 * @throws Exception
	 */
	@Test
	public void runTest() throws Exception {
		BPMN2Process process = new BPMN2Process("BPMN2-InclusiveSplit");
		process.addDataType("Integer");
		process.addLocalVariable("x", "Integer");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("Gateway", ConstructType.INCLUSIVE_GATEWAY);
		
		InclusiveGateway gateway = new InclusiveGateway("Gateway");
		gateway.append("Script1", ConstructType.SCRIPT_TASK, Position.NORTH);
		gateway.append("Script2", ConstructType.SCRIPT_TASK);
		gateway.append("Script3", ConstructType.SCRIPT_TASK, Position.SOUTH);
		gateway.setCondition("Gateway -> Script1", "java", "return x > 0;");
		gateway.setCondition("Gateway -> Script2", "java", "return x > 10;");
		gateway.setCondition("Gateway -> Script3", "java", "return x > 20;");

		ScriptTask script1 = new ScriptTask("Script1");
		script1.setScript("Java", "System.out.println(\"path1\");");
		script1.append("EndProcess1", ConstructType.END_EVENT);
		
		ScriptTask script2 = new ScriptTask("Script2");
		script2.setScript("Java", "System.out.println(\"path2\");");
		script2.append("EndProcess2", ConstructType.END_EVENT);

		ScriptTask script3 = new ScriptTask("Script3");
		script3.setScript("Java", "System.out.println(\"path3\");");
		script3.append("EndProcess3", ConstructType.END_EVENT);
		
		/*
		 * TODO - test code bellow. EndEvent should allow more than 1 incomming connection.
		 */
//		ScriptTask script2 = new ScriptTask("Script2");
//		script2.setScript("Java", "System.out.println(\"path2\");");
//		script2.append("EndProcess", ConstructType.END_EVENT);
//
//		EndEvent end = new EndEvent("EndProcess");
//		
//		ScriptTask script1 = new ScriptTask("Script1");
//		script1.setScript("Java", "System.out.println(\"path1\");");
//		script1.connectTo(end);
//
//		ScriptTask script3 = new ScriptTask("Script3");
//		script3.setScript("Java", "System.out.println(\"path3\");");
//		script3.connectTo(end);
	}
	
}