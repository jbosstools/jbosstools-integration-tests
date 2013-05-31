package org.jboss.tools.bpmn2.itests.test;

import org.apache.log4j.Logger;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.tools.bpmn2.itests.editor.BPMN2Editor;
import org.jboss.tools.bpmn2.itests.reddeer.EclipseHelper;
import org.jboss.tools.bpmn2.itests.reddeer.requirements.ProcessDefinitionRequirement.ProcessDefinition;
import org.jboss.tools.bpmn2.itests.validator.JBPM6Validator;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

/**
 * 
 * @author Marek Baluch <mbaluch@redhat.com>
 */
public class JBPM6BaseTest extends SWTBotTestCase {

	protected Logger log = Logger.getLogger(getClass());  
	
	private ProcessDefinition processDefinition;
	
	private String editorTitle; 
	
	public JBPM6BaseTest() {
		/*
		 * Initialize
		 */
		processDefinition = getClass().getAnnotation(ProcessDefinition.class);
		if (processDefinition == null) {
			throw new RuntimeException("Validation failed. Missing @ProcessDefinition annotation.");
		}
		editorTitle = processDefinition.name().replace("\\s+", "");
	}
	
	@Before
	public void open() {
		/*
		 * Open process definition.
		 */
		EclipseHelper.maximizeActiveShell();
		new PackageExplorer().getProject(processDefinition.project()).getProjectItem(editorTitle + ".bpmn2").open();
		/*
		 * Activate requested editing profile.
		 */
		new BPMN2Editor(editorTitle).activateTool("Profiles", processDefinition.profile());
	}
	
	@After
	public void close() {
		BPMN2Editor editor = new BPMN2Editor(editorTitle);
		editor.setFocus();
		/*
		 * Validate
		 */
		log.info("Validating '" + editorTitle + "'");
		JBPM6Validator validator = new JBPM6Validator();
		boolean result = validator.validate(editor.getSourceText());
		Assert.assertTrue(validator.getResultMessage(), result);
		/*
		 * Close
		 */
		editor.close();
	}
	
}
