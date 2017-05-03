/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.freemarker.ui.bot.test.editor;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.reddeer.common.logging.Logger;
import org.jboss.reddeer.common.util.Display;
import org.jboss.reddeer.eclipse.core.resources.Project;
import org.jboss.reddeer.eclipse.core.resources.ProjectItem;
import org.jboss.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.jboss.reddeer.jface.text.contentassist.ContentAssistant;
import org.jboss.reddeer.junit.runner.RedDeerSuite;
import org.jboss.reddeer.workbench.impl.editor.TextEditor;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Freemarker test for code assist 
 * @author Jiri Peterka
 *
 */
@RunWith(RedDeerSuite.class)
public class FreeMarkerCodeAssistTest extends AbstractFreemarkerTest  {
	
	
	private static final Logger log = Logger.getLogger(FreeMarkerCodeAssistTest.class);
	
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

	@Test 
	public void codeAssistElseTest() {
		checkCodeAssist("<#", "else");
	}
	
	@Test 
	public void codeAssistElseIfTest() {
		checkCodeAssist("<#", "elseif");
	}

	@Test 
	public void codeAssistEscapeTest() {
		checkCodeAssist("<#", "escape");
	}

	@Test 
	public void codeAssistFallbackTest() {
		checkCodeAssist("<#", "fallback");
	}

	@Test 
	public void codeAssistFlushTest() {
		checkCodeAssist("<#", "flush");
	}

	@Test 
	public void codeAssistForeachTest() {
		checkCodeAssist("<#", "foreach");
	}
	
	@Test 
	public void codeAssistFtlTest() {
		checkCodeAssist("<#", "ftl");
	}
	
	@Test 
	public void codeAssistFuctionTest() {
		checkCodeAssist("<#", "function");
	}
	@Test 
	public void codeAssistGlobaTest() {
		checkCodeAssist("<#", "global");
	}

	@Test 
	public void codeAssistIfTest() {
		checkCodeAssist("<#", "if");
	}
	
	@Test 
	public void codeAssistImportTest() {
		checkCodeAssist("<#", "import");
	}
	
	@Test 
	public void codeAssistIncludeTest() {
		checkCodeAssist("<#", "include");
	}
	
	@Test 
	public void codeAssisttListTest() {
		checkCodeAssist("<#", "list");
	}

	@Test 
	public void codeAssistLocalTest() {
		checkCodeAssist("<#", "local");
	}
	
	@Test 
	public void codeAssistLtTest() {
		checkCodeAssist("<#", "lt");
	}
	
	@Test 
	public void codeAssistMacroTest() {
		checkCodeAssist("<#", "macro");
	}
	
	@Test 
	public void codeAssistNestedTest() {
		checkCodeAssist("<#", "nested");
	}
	
	@Test 
	public void codeAssistNoescapeTest() {
		checkCodeAssist("<#", "noescape");
	}
	
	@Test 
	public void codeAssistNoparseTest() {
		checkCodeAssist("<#", "noparse");
	}
	
	@Test 
	public void codeAssistNtTest() {
		checkCodeAssist("<#", "nt");
	}
	
	@Test 
	public void codeAssistRecoverTest() {
		checkCodeAssist("<#", "recover");
	}
	
	@Test 
	public void codeAssistReturnTest() {
		checkCodeAssist("<#", "return");
	}
	
	@Test 
	public void codeAssistRtTest() {
		checkCodeAssist("<#", "rt");
	}
	
	@Test 
	public void codeAssistSettingTest() {
		checkCodeAssist("<#", "setting");
	}

	@Test 
	public void codeAssistStopTest() {
		checkCodeAssist("<#", "stop");
	}
	
	@Test 
	public void codeAssistSwitchTest() {
		checkCodeAssist("<#", "switch");
	}

	@Test 
	public void codeAssistTTest() {
		checkCodeAssist("<#", "t");
	}

	@Test 
	public void codeAssistVisitTest() {
		checkCodeAssist("<#", "visit");
	}

	private void checkCodeAssist(String expr, String... expected) {
		log.step("Check if code assist contains expected expressions");		
		
		ProjectExplorer explorer = new ProjectExplorer();
		Project project = explorer.getProject(projectName);
		project.expand();
		project.refresh();
		ProjectItem item = project.getProjectItem(parentFolder, "empty.ftl");
		item.open();

		String file = "empty.ftl";
		TextEditor textEditor = new TextEditor(file);
		String start = expr;		
		textEditor.setText(start);
		textEditor.save();				
		
		forceEditorFocus(textEditor);
		textEditor.setCursorPosition(start.length());
				
		ContentAssistant ca = textEditor.openContentAssistant();
		List<String> proposals = ca.getProposals();
		ca.close();
		
		for (String e : expected) {			
			assertTrue(e + " is expected", proposals.contains(e));
		}

	}

	private void forceEditorFocus(TextEditor editor) {
		Display.syncExec(new Runnable() {

            @Override
            public void run() {
                editor.getEditorPart().setFocus();
            }
        });		
	}
	
}
