package org.jboss.tools.bpmn2.itests.test.editor;

import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.test.JBPM6BaseTest;
import org.junit.Ignore;
import org.junit.Test;

/**
 *     
 * @author mbaluch
 */
@ProcessDefinition(name="BPMN2-CallActivityByName", project="EditorTestProject")
public class CallActivityByNameTest extends JBPM6BaseTest {

	/**
	 * ISSUES: There's no way to set the 'call by name' attribute. Is this a BUG?
	 *  
	 * @throws Exception
	 */
	@Ignore
	@Test
	public void runTest() throws Exception {
	}
	
}