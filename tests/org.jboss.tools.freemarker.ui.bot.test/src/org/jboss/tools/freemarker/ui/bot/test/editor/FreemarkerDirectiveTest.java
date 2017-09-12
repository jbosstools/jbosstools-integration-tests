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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.reddeer.common.logging.Logger;
import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.core.resources.Project;
import org.eclipse.reddeer.eclipse.core.resources.ProjectItem;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.workbench.core.condition.JobIsRunning;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Freemarker Directives tests
 * @author Jiri Peterka
 *
 */
@RunWith(RedDeerSuite.class)
public class FreemarkerDirectiveTest extends AbstractFreemarkerTest {
	
	
	private static final Logger log = Logger.getLogger(FreeMarkerBaseEditorTest.class);
	
	@Before
	public void before() {
		emptyErrorLog();
		log.step("Open ftl file in freemarker editor");
	}

	@Test
	public void assignDirectiveOutlineTest() {
		openFTLFileInEditor("assign-directive.ftl","assign variable1=value1 variable2=value2");		
		checkErrorLog();
	}	

	@Test
	public void attemptDirectiveOutlineTest() {
		openFTLFileInEditor("attempt-directive.ftl","attempt");		
		checkErrorLog();
	}
	
	@Test
	public void autoescDirectiveOutlineTest() {
		openFTLFileInEditor("autoesc-directive.ftl", "ftl output_format=\"XML\" auto_esc=true", 
							"noautoesc", "autoesc");		
		checkErrorLog();
	}	
	
	@Test
	public void compressDirectiveOutlineTest() {
		openFTLFileInEditor("compress-directive.ftl","compress");		
		checkErrorLog();		
	}
		
	@Test
	public void ftlDirectiveOutlineTest() {
		openFTLFileInEditor("ftl-directive.ftl","ftl param1=value1 param2=value2");		
		checkErrorLog();		
	}
	
	@Test
	public void flushDirectiveOutlineTest() {
		openFTLFileInEditor("flush-directive.ftl","flush");		
		checkErrorLog();		
	}

	@Test
	public void functionDirectiveOutlineTest() {
		openFTLFileInEditor("function-directive.ftl",
				"function avg x y",
				"return (x + y) / 2",
				"function avg nums...",
				"local sum = 0",
				"list nums as num",
				"local sum += num",
				"if nums?size != 0",
				"return sum / nums?size",
				"avg(10, 20, 30, 40)",
				"avg()!\"N/A\"");		
		checkErrorLog();		
	}
	
	@Test
	public void globalDirectiveOutlineTest() {
		openFTLFileInEditor("global-directive.ftl","global var1=value1 var2=value2");		
		checkErrorLog();
	}	

	@Test
	public void ifDirectiveOutlineTest() {
		openFTLFileInEditor("if-directive.ftl","if x == 1","else");		
		checkErrorLog();		
	}

	@Test
	public void importCallDirectiveOutlineTest() {
		openFTLFileInEditor("import-call-directive.ftl","my.copyright date=\"1999-2002\"");		
		checkErrorLog();
	}	
		
	@Test
	public void importDefDirectiveOutlineTest() {
		openFTLFileInEditor("import-def-directive.ftl","import \"/libs/mylib.ftl\" as my");		
		checkErrorLog();
	}	
	
	@Test
	public void includeDirectiveOutlineTest() {
		openFTLFileInEditor("include-directive.ftl","include \"/resource/copyright.ftl\"");		
		checkErrorLog();
	}	

	@Test
	public void listDirectiveOutlineTest() {
		openFTLFileInEditor("list-directive.ftl", "assign users=[\"Alan\", \"Benjamin\", \"Cecile\"]",
				"list users", "items as user", "user", "else");		
		checkErrorLog();		
	}
	
	@Test
	public void localDirectiveOutlineTest() {
		openFTLFileInEditor("local-directive.ftl","local name1=value1 name2=value2");		
		checkErrorLog();
	}	

	@Test
	public void macroDefDirectiveOutlineTest() {
		openFTLFileInEditor("macro-def-directive.ftl","test");		
		checkErrorLog();
	}	

	@Test
	public void macroCallDirectiveOutlineTest() {
		openFTLFileInEditor("macro-call-directive.ftl","test");		
		checkErrorLog();
	}
	
	@Test
	public void noparseDirectiveOutlineTest() {
		openFTLFileInEditor("noparse-directive.ftl","noparse");		
		checkErrorLog();
	}
	
	@Test
	public void settingDirectiveOutlineTest() {
		openFTLFileInEditor("setting-directive.ftl", "setting locale=\"cs\"", "setting boolean_format=\"Y,N\"");
		checkErrorLog();
	}	
	
	@Test
	public void stopDirectiveOutlineTest() {
		openFTLFileInEditor("stop-directive.ftl", "stop reason=\"No reason.\"");
		checkErrorLog();
	}
	
	@Test
	public void switchDirectiveOutlineTest() {
		openFTLFileInEditor("switch-directive.ftl", "switch animal.size", "case \"small\"", "break", "default");
		checkErrorLog();
	}
	
	@Test
	public void trimDirectiveOutlineTest() {
		openFTLFileInEditor("trim-directive.ftl", "nt", "t", "lt", "rt");
		checkErrorLog();
	}
	
	@Test
	public void visitDirectiveOutlineTest() {
		openFTLFileInEditor("visit-directive.ftl", "import \"/lib/docbook.ftl\" as docbook", 
							"visit document using [.namespace, docbook]", "recurse");
		checkErrorLog();
	}


	private void openFTLFileInEditor(String file, String... outline) {
		
		ProjectExplorer explorer = new ProjectExplorer();
		Project project = explorer.getProject(projectName);
		project.expand();
		project.refresh();
		ProjectItem item = project.getProjectItem(parentFolder, file);
		item.open();
		
		new TextEditor(file);
		
		log.step("Open outline view and check freemarker elements there");
		ContentOutline ov = new ContentOutline();
		ov.open();
		
		Collection<TreeItem> outlineElements = ov.outlineElements();
		List<String> list = new ArrayList<String>();
		getOutlineTree(list, outlineElements);
		
		for (String o : outline ) {			
			assertTrue("Outline is expected to contain \"" + o + "\"", list.contains(o));
		}
	}
	
	// returns whole tree expanded as a list of strings
	private void getOutlineTree(List<String> list, Collection<TreeItem> tree) {
		for (TreeItem item : tree) {
			list.add(item.getText());
			item.expand();
			getOutlineTree(list, item.getItems());
		}
	}

	@AfterClass
	public static void aterClass() {
		new WaitWhile(new JobIsRunning());
	}
}
