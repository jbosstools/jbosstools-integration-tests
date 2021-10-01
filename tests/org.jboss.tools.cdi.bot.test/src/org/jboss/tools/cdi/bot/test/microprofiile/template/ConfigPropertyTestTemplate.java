/*******************************************************************************
 * Copyright (c) 2021 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.cdi.bot.test.microprofiile.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.jboss.tools.maven.ui.bot.test.utils.MavenProjectHelper.addDependency;
import static org.jboss.tools.maven.ui.bot.test.utils.MavenProjectHelper.convertToMavenProject;

import org.eclipse.reddeer.common.wait.WaitWhile;
import org.eclipse.reddeer.eclipse.jdt.ui.packageview.PackageExplorerPart;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView;
import org.eclipse.reddeer.eclipse.ui.views.markers.ProblemsView.ProblemType;
import org.eclipse.reddeer.workbench.condition.EditorHasValidationMarkers;
import org.eclipse.reddeer.workbench.impl.editor.TextEditor;
import org.jboss.tools.cdi.bot.test.CDITestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author zcervink@redhat.com
 * 
 */
public class ConfigPropertyTestTemplate extends CDITestBase {

	@Before
	public void prepareProject() {
		PackageExplorerPart pexplorer = new PackageExplorerPart();
		pexplorer.open();
		pexplorer.getProject(PROJECT_NAME).select();

		beansHelper.createClass(CDI_BEAN_1_JAVA_FILE_NAME, PACKAGE_NAME);
		editResourceUtil.replaceClassContentByResource(CDI_BEAN_1_JAVA_FILE_NAME + JAVA_FILE_EXTENSION,
				readFile("resources/configProperty/CdiBean1.jav_"), false, false);
		TextEditor te = new TextEditor(CDI_BEAN_1_JAVA_FILE_NAME + JAVA_FILE_EXTENSION);
		new WaitWhile(new EditorHasValidationMarkers(te));
		te.save();
		new WaitWhile(new EditorHasValidationMarkers(te));

		convertToMavenProject(PROJECT_NAME, "war", false);
	}

	@Test
	public void testConfigPropertyAnnotation() {
		PackageExplorerPart pexplorer = new PackageExplorerPart();
		pexplorer.open();
		pexplorer.getProject(PROJECT_NAME)
				.getProjectItem("src/main/java", PACKAGE_NAME, CDI_BEAN_1_JAVA_FILE_NAME + JAVA_FILE_EXTENSION).open();
		ProblemsView pw = new ProblemsView();
		pw.open();
		int error_count = pw.getProblems(ProblemType.ERROR).size();
		String error_msgs = pw.getProblems(ProblemType.ERROR).toString();
		assertTrue(
				"When missing Microprofile dependency in the pom.xml there should be 2 errors in the Problems View ('ConfigProperty cannot be resolved to a type' and 'The import org.eclipse cannot be resolved').",
				error_count == 2 && error_msgs.contains("ConfigProperty cannot be resolved to a type")
						&& error_msgs.contains("The import org.eclipse cannot be resolved"));

		addDependency(PROJECT_NAME, "org.eclipse.microprofile.config", "microprofile-config-api", "2.0");

		pexplorer.getProject(PROJECT_NAME)
				.getProjectItem("src/main/java", PACKAGE_NAME, CDI_BEAN_1_JAVA_FILE_NAME + JAVA_FILE_EXTENSION).open();
		pw.open();
		error_count = pw.getProblems(ProblemType.ERROR).size();
		assertEquals("There are errors after including the Microprofile @ConfigProperty annotation into the project - "
				+ pw.getProblems(ProblemType.ERROR).toString(), error_count, 0);
	}
}
