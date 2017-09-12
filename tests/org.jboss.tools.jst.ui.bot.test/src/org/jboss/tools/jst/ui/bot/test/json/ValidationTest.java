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

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.reddeer.common.wait.WaitUntil;
import org.eclipse.reddeer.eclipse.ui.navigator.resources.ProjectExplorer;
import org.eclipse.reddeer.eclipse.ui.problems.Problem;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.workbench.condition.EditorHasValidationMarkers;
import org.eclipse.reddeer.workbench.handler.WorkbenchShellHandler;
import org.eclipse.reddeer.workbench.impl.editor.Marker;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.jst.ui.bot.test.JSTTestBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Tests for validation in JSON Editor 
 * 
 * @author Pavol Srna
 *
 */
public class ValidationTest extends JSTTestBase {

	
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
	public void testErrorMarkersShown(){
		TextEditor editor = new TextEditor("package.json");
		editor.activate();
		
		assertTrue("Error marker is showing where should not!", editor.getMarkers().size() == 0);
		
		//trigger error
		editor.insertText(0, "{");
		editor.save();
		
		new WaitUntil(new EditorHasValidationMarkers(editor));
		
		List<Marker> markers = editor.getMarkers();
		assertTrue("Error not detected!", markers.size() == 1);
		assertThat(markers.get(0).getText(), is("Expected name at 1:1"));
		
	}
	
	@Test
	public void testProblemsShownInView() {
		
		ProblemsView pv = new ProblemsView();
 		assertTrue("Error detected in Problems View!", pv.getProblems(ProblemType.ERROR).size() == 0);
		
		TextEditor editor = new TextEditor("package.json");
		editor.activate();
		//trigger error
		editor.insertText(0, "{");
		editor.save();
		
		new WaitUntil(new EditorHasValidationMarkers(editor));
		
		pv.activate();
		List<Problem> problems = pv.getProblems(ProblemType.ERROR);
		assertTrue("Error not detected!", problems.size() == 1);
		assertThat(problems.get(0).getDescription(), is("Expected name at 1:1"));
 		
	}
	
}
