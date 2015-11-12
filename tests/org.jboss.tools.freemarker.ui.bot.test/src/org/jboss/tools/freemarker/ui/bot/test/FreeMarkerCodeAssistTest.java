package org.jboss.tools.freemarker.ui.bot.test;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.eclipse.jdt.ui.ProjectExplorer;
import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Freemarker test for code assist 
 * @author Jiri Peterka
 *
 */
@RunWith(RedDeerSuite.class)
public class FreeMarkerCodeAssistTest extends FreemarkerTest  {
	
	
	private static final Logger log = Logger.getLogger(FreeMarkerCodeAssistTest.class);
	private String prj = "org.jboss.tools.freemarker.testprj";
	
	@BeforeClass
	public static void beforeClass() {
	}
	
	@Before
	public void before() {
		log.step("Import test project for freemarker test");
		importTestProject();
		log.step("Open ftl file in freemarker editor");		
	}
	
	@Test
	public void freeCodeAssistDirectiveAssign() {		
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		new DefaultTreeItem(prj, "ftl", "empty.ftl").doubleClick();				
		
		pe.open();
		new DefaultTreeItem(prj, "ftl", "empty.ftl").doubleClick();				

		String file = "empty.ftl";
		TextEditor textEditor = new TextEditor(file);
		String start = "<#";		
		textEditor.setText(start);
		textEditor.save();				
		
		textEditor.setCursorPosition(start.length());
				
		String proposal = "assign";
		ContentAssistant ca = textEditor.openContentAssistant();
		List<String> proposals = ca.getProposals();
		ca.close();
		assertTrue(proposal + " is expected", proposals.contains(proposal));

	}	

	@After
	public void after() {
	}
}
