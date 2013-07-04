package org.jboss.tools.bpmn2.itests.test.editor;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.Position;
import org.jboss.tools.bpmn2.itests.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.itests.editor.jbpm.activities.SubProcess;
import org.jboss.tools.bpmn2.itests.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;
import org.junit.Test;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-SubProcess", project="EditorTestProject")
public class SubProcessTest extends JBPM6BaseTest {

	/**
	 *
	 *  
	 * @throws Exception
	 */
	@Test
	public void runTest() throws Exception {
		StartEvent start = new StartEvent("StartProcess");
		start.append("Hello Subprocess", ConstructType.SUB_PROCESS);

		SubProcess subProcess = new SubProcess("Hello Subprocess");
		subProcess.addLocalVariable("x", "String");
		subProcess.append("Goodbye", ConstructType.SCRIPT_TASK);

		ScriptTask script4 = new ScriptTask("Goodbye");
		script4.setScript("Java", "System.out.println(\"Goodbye World\");");
		script4.append("EndProcess", ConstructType.TERMINATE_END_EVENT);
		
		/*
		 * Now create the inner of the sub process.
		 */
		subProcess.add("StartSubProcess", ConstructType.START_EVENT);
		
		StartEvent start2 = new StartEvent("StartSubProcess");
		start2.append("Hello1", ConstructType.SCRIPT_TASK);
		
		ScriptTask script1 = new ScriptTask("Hello1");
		script1.setScript("Java", "System.out.println(\"x = \" + x)");
		
		/*
		 * Goes out of bounds :(.
		 */
//		script1.append("Hello2", ConstructType.SCRIPT_TASK, Position.SOUTH);
//		
//		ScriptTask script2 = new ScriptTask("Hello2");
//		script2.setScript("Java", "kcontext.setVariable(\"x\", \"Hello\");");
//		script2.append("Hello3", ConstructType.SCRIPT_TASK, Position.SOUTH);
//		
//		ScriptTask script3 = new ScriptTask("Hello3");
//		script3.setScript("Java", "System.out.println(\"x = \" + x)");
//		script3.append("EndSubProcess", ConstructType.END_EVENT);
		
		/*
		 * Just one script task must be sufficient.
		 */
		script1.append("EndSubProcess", ConstructType.END_EVENT, Position.SOUTH);
	}
	
}