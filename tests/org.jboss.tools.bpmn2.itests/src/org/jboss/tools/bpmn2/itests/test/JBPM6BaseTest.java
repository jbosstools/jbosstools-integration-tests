package org.jboss.tools.bpmn2.itests.test;

import org.apache.log4j.Logger;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.jboss.reddeer.eclipse.jdt.ui.packageexplorer.PackageExplorer;
import org.jboss.tools.bpmn2.itests.reddeer.BPMN2Editor;
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
	
	private ProcessDefinition definition;
	
	private BPMN2Editor editor; 
	
	public JBPM6BaseTest() {
		/*
		 * Initialize
		 */
		definition = getClass().getAnnotation(ProcessDefinition.class);
		if (definition == null) {
			throw new RuntimeException("Validation failed. Missing @ProcessDefinition annotation.");
		}
		
		editor = new BPMN2Editor(definition.name().replace("\\s+", ""));
	}
	
	@Before
	public void open() {
		/*
		 * Open process definition.
		 */
		EclipseHelper.maximizeActiveShell();
		new PackageExplorer().getProject(definition.project()).getProjectItem(editor.getTitle() + ".bpmn2").open();
		/*
		 * Activate requested editing profile.
		 */
		editor.activateTool("Profiles", definition.profile());
	}
	
	@After 
	public void closeDialogs() {
		new SWTWorkbenchBot().closeAllShells();
	}
	
	@After
	public void close() {
		try {
			/*
			 * Make sure all content is saved.
			 */
			editor.setFocus();
			if (editor.isDirty()) editor.save();
			/*
			 * Validate.
			 */
			log.info("Validating '" + editor.getTitle() + "'");
			JBPM6Validator validator = new JBPM6Validator();
			boolean result = validator.validate(editor.getSourceText());
			Assert.assertTrue(validator.getResultMessage(), result);
		} finally {
			/*
			 * Close.
			 */
			log.info("Closing '" + editor.getTitle() + "'");
			editor.close();
		}
	}
	
	public BPMN2Editor getEditor() {
		return editor;
	}
	
}
