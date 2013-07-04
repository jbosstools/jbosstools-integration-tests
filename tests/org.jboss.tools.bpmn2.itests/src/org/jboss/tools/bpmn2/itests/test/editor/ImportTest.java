package org.jboss.tools.bpmn2.itests.test.editor;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.jbpm.BPMN2Process;
import org.jboss.tools.bpmn2.itests.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.itests.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;
import org.junit.Test;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-Import", project="EditorTestProject")
public class ImportTest extends JBPM6BaseTest {

	/**
	 *
	 *  
	 * @throws Exception
	 */
	@Test
	public void runTest() throws Exception {
		BPMN2Process process = new BPMN2Process("BPMN2-Import");
//		process.addImport("java.util.List");
		process.addImport("java.util.ArrayList");
		
		StartEvent start = new StartEvent("StartProcess");
		start.append("Hello", ConstructType.SCRIPT_TASK);
		
		ScriptTask script = new ScriptTask("Hello");
		script.setScript("Java", "ArrayList l = new ArrayList(); System.out.println(l);");
		script.append("EndProcess", ConstructType.TERMINATE_END_EVENT);
	}
	
}