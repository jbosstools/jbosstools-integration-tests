package org.jboss.tools.bpmn2.itests.test.editor;

import org.jboss.tools.bpmn2.itests.editor.ConstructType;
import org.jboss.tools.bpmn2.itests.editor.Position;
import org.jboss.tools.bpmn2.itests.editor.jbpm.BPMN2Process;
import org.jboss.tools.bpmn2.itests.editor.jbpm.activities.ScriptTask;
import org.jboss.tools.bpmn2.itests.editor.jbpm.dataobjects.DataObject;
import org.jboss.tools.bpmn2.itests.editor.jbpm.startevents.StartEvent;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;
import org.junit.Test;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-DataObject", project="EditorTestProject")
public class DataObjectTest extends JBPM6BaseTest {

	/**
	 *
	 *  
	 * @throws Exception
	 */
	@Test
	public void runTest() throws Exception {
		StartEvent start = new StartEvent("StartProcess");
		start.append("Script", ConstructType.SCRIPT_TASK);
		
		ScriptTask script = new ScriptTask("Script");
		/*
		 * ISSUE:
		 * 	1) some users may use &quot; instead of ". The validator will not replace the entities.
		 */
		script.setScript("Java", "System.out.println(\"Processing evaluation for employee \" + employee);");
		script.append("EndProcess", ConstructType.TERMINATE_END_EVENT);
		
		BPMN2Process process = new BPMN2Process("BPMN2-DataObject");
		process.add("employee", ConstructType.DATA_OBJECT, start, Position.SOUTH);
		
		DataObject object = new DataObject("employee");
		object.setDataObjectType("String");
	}
	
}