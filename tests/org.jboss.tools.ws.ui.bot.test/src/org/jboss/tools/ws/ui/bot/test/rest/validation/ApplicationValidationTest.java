/*******************************************************************************
 * Copyright (c) 2010-2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.ws.ui.bot.test.rest.validation;

import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.junit.runner.RedDeerSuite;
import org.eclipse.reddeer.requirements.autobuilding.AutoBuildingRequirement.AutoBuilding;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.ws.ui.bot.test.rest.RESTfulTestBase;
import org.jboss.tools.ws.ui.bot.test.utils.ProjectHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 
 * @author jjankovi
 *
 */
@RunWith(RedDeerSuite.class)
@AutoBuilding(value = false, cleanup = true)
public class ApplicationValidationTest extends RESTfulTestBase {

	
	@Before
	public void setup() {
		
	}

	@Test
	public void testMultipleApplicationClasses() {
		final String projectName = "app1";

		/* prepare project */
		importWSTestProject(projectName);
		ProjectHelper.cleanAllProjects();
		
		/* test validation error */
		assertCountOfValidationProblemsExists(ProblemType.ERROR, projectName, "Multiple JAX-RS Activators", null, 2);		
	}
	
	@Test
	public void testWebXmlAndApplicationClassWithWarning() {
		final String projectName = "app2";

		/* prepare project */
		importWSTestProject(projectName);
		ProjectHelper.cleanAllProjects();

		/* test validation error */
		assertCountOfValidationProblemsExists(ProblemType.ERROR, projectName, null, null, 2);
		assertCountOfValidationProblemsExists(ProblemType.ERROR, projectName, "Multiple JAX-RS Activators", null, 2);
	}
	
	@Test
	public void testWebXmlAndApplicationClassWithoutWarning() {
		final String projectName = "app3";

		/* prepare project */
		importWSTestProject(projectName);
		ProjectHelper.cleanAllProjects();

		/* test validation error */
		assertCountOfValidationProblemsExists(ProblemType.WARNING, projectName, null, null, 0);
		assertCountOfValidationProblemsExists(ProblemType.ERROR, projectName, null, null, 0);
	}
	
	@Test
	public void testNotExtendingApplicationClass() {
		final String projectName = "app4";

		/* prepare project */
		importWSTestProject(projectName);
		ProjectHelper.cleanAllProjects();

		/* test validation error */
		assertCountOfValidationProblemsExists(ProblemType.ERROR, projectName, null, null, 1);

		/* fix class - should be no error */
		openJavaFile(projectName, "test", "App.java");
		TextEditor editor = new TextEditor();
		editor.setText(editor.getText().replace("@ApplicationPath(\"/rest\")", ""));
		editor.save();
		ProjectHelper.cleanAllProjects();

		/* test validation error */
		assertCountOfValidationProblemsExists(ProblemType.ERROR, projectName, null, null, 0);
	}

	@Test
	public void testApplicationClassWithoutPath() {
		final String projectName = "app5";

		/* prepare project */
		importWSTestProject(projectName);
		ProjectHelper.cleanAllProjects();

		/* test validation error */
		assertCountOfValidationProblemsExists(ProblemType.ERROR, projectName, null, null, 1);

		/* fix class - should be no error */
		openJavaFile(projectName, "test", "App.java");
		TextEditor editor = new TextEditor();
		editor.setText(editor.getText().replace("extends Application", ""));
		editor.save();
		ProjectHelper.cleanAllProjects();

		/* test validation error */
		assertCountOfValidationProblemsExists(ProblemType.ERROR, projectName, null, null, 0);
	}
}
