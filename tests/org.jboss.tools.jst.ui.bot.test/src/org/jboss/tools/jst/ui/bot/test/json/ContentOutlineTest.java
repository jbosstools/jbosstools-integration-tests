/******************************************************************************* 
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jst.ui.bot.test.json;

import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.core.handler.ShellHandler;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.reddeer.swt.api.TreeItem;
import org.eclipse.reddeer.swt.condition.TreeItemHasMinChildren;
import org.eclipse.reddeer.swt.impl.tree.DefaultTree;
import org.eclipse.reddeer.workbench.handler.WorkbenchShellHandler;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.jst.ui.bot.test.JSTTestBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertTrue;



/**
 * Tests for JSON Editor and Outline view
 * 
 * @author Pavol Srna
 *
 */
public class ContentOutlineTest extends JSTTestBase {

	private static String SIMPLE_TAG = "name";
	private static String NESTED_TAG = "echo";
	
	@Before
	public void prepare() {
		createJSProject(PROJECT_NAME);
		npmInit(); //create package.json
		
	}

	@After
	public void cleanup() {
		WorkbenchShellHandler.getInstance().closeAllNonWorbenchShells();
		new ProjectExplorer().deleteAllProjects();
	}

	
	@Test
	public void testSimpleTagSelectedInOutline() throws IOException {
		testTagSelectedInOutline(SIMPLE_TAG);	
	}
	 
	@Test
	public void testNestedTagSelectedInOutline() throws IOException {
		testTagSelectedInOutline(NESTED_TAG);
	}
	
	private void testTagSelectedInOutline(String TAG){
		
		TextEditor editor = new TextEditor("package.json");
		editor.activate();
		
		ContentOutline view = new ContentOutline();
		view.activate();
		DefaultTree t = new DefaultTree();

		editor.activate();
		editor.selectText(TAG);
		new WaitUntil(new TreeItemHasMinChildren(t.getItems().get(0), 1));

		List<TreeItem> s = t.getSelectedItems();
		assertTrue("No selected items in outline view!", !s.isEmpty());
		assertTrue("'" + TAG + "' is not selected in outline view!", s.get(0).getText().contains(TAG));
	}

}
