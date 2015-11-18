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
import org.junit.AfterClass;
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
	private static String prj = "org.jboss.tools.freemarker.testprj";
	
	@BeforeClass
	public static void beforeClass() {
		log.step("Import test project for freemarker test");
		importTestProject();
		log.step("Open ftl file in freemarker editor");		
	}
	
	@Test
	public void codeAssistDirectiveAssign() {
		String expr = "<#";
		checkCodeAssist(expr, "assign");
	}
	
	@Test
	public void codeAssistAssignTest() {
		String expr = "<#assign var1=1><#assign var2=2><#assign va";
		checkCodeAssist(expr, "var1", "var2");
	}
	
	@Test 
	public void codeAssistAttemptTest() {
		checkCodeAssist("<#", "attempt");
	}
	
	@Test 
	public void codeAssistBreakTest() {
		checkCodeAssist("<#", "break");
	}
	
	@Test 
	public void codeAssistCaseTest() {
		checkCodeAssist("<#", "case");
	}

	@Test 
	public void codeAssistCompressTest() {
		checkCodeAssist("<#", "compress");
	}

	@Test 
	public void codeAssistDefaultTest() {
		checkCodeAssist("<#", "default");
	}
	
	@After
	public void after() {
	}

	private void checkCodeAssist(String expr, String... expected) {
		ProjectExplorer pe = new ProjectExplorer();
		pe.open();
		new DefaultTreeItem(prj, "ftl", "empty.ftl").doubleClick();				
		
		// editor focus bug workaround 
		pe.open();
		new DefaultTreeItem(prj, "ftl", "empty.ftl").doubleClick();				

		String file = "empty.ftl";
		TextEditor textEditor = new TextEditor(file);
		String start = expr;		
		textEditor.setText(start);
		textEditor.save();				
		
		textEditor.setCursorPosition(start.length());
				
		ContentAssistant ca = textEditor.openContentAssistant();
		List<String> proposals = ca.getProposals();
		ca.close();
		
		for (String e : expected) {			
			assertTrue(e + " is expected", proposals.contains(e));
		}
	}
	
	@AfterClass
	public static void cleanup() {
		removeTestProject(prj);
	}

}
