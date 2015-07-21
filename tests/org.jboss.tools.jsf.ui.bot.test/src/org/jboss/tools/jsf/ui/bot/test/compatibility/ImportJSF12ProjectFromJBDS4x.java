/*******************************************************************************
 * Copyright (c) 2007-2015 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jsf.ui.bot.test.compatibility;

import java.io.File;
import java.io.IOException;

import org.jboss.reddeer.common.matcher.RegexMatcher;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView;
import org.jboss.reddeer.eclipse.ui.problems.ProblemsView.ProblemType;
import org.jboss.reddeer.eclipse.ui.problems.matcher.ProblemsPathMatcher;
import org.jboss.reddeer.swt.impl.button.CheckBox;
import org.jboss.tools.jsf.reddeer.ui.wizard.project.ImportProjectWizard;
import org.jboss.tools.jsf.ui.bot.test.JSFAutoTestCase;
import org.jboss.tools.jst.reddeer.web.ui.wizards.project.ImportWebProjectWizardPage;
import org.jboss.tools.ui.bot.ext.helper.FileHelper;
import org.junit.Test;

/**
 * Test importing JSF 1.2 project created using JBDS 4.1.x
 * 
 * @author Vladimir Pakan
 *
 */
public class ImportJSF12ProjectFromJBDS4x extends JSFAutoTestCase {

	private static final String PROJECT_TO_IMPORT_NAME = "jsf12importtest";

	@Test
	public void testImportJSFProject() {
		// copy and unzip JSF 1.2 project to tmp directory
		try {
			final String projectToImportZipName = ImportJSF12ProjectFromJBDS4x.PROJECT_TO_IMPORT_NAME + ".zip";
			final String resourceProjectToImportLocation = getPathToResources(
					"projects" + File.separator + projectToImportZipName);
			final String tmpDir = System.getProperty("java.io.tmpdir", ".");
			FileHelper.copyFilesBinary(new File(resourceProjectToImportLocation), new File(tmpDir));
			FileHelper.unzipArchive(new File(tmpDir, projectToImportZipName), new File(tmpDir));
			final String projectToImportLocation = tmpDir + File.separator
					+ ImportJSF12ProjectFromJBDS4x.PROJECT_TO_IMPORT_NAME;
			ImportProjectWizard importProjectWizard = new ImportProjectWizard();
			importProjectWizard.open();
			new ImportWebProjectWizardPage().setWebXmlLocation(projectToImportLocation + File.separator + "WebContent"
					+ File.separator + "WEB-INF" + File.separator + "web.xml");
			importProjectWizard.next();
			// do not deploy to Server
			new CheckBox(1).toggle(false);
			importProjectWizard.finish();
			packageExplorer.open();
			// check if project is present within package explorer
			assertTrue(
					"Imported project " + ImportJSF12ProjectFromJBDS4x.PROJECT_TO_IMPORT_NAME
							+ " is not present in Packag Explorer",
					packageExplorer.containsProject(PROJECT_TO_IMPORT_NAME));
			ProblemsView problemsView = new ProblemsView();
			problemsView.open();
			// check if imported project has no errors and no warnings
			assertTrue(
					"There were errors when importing " + ImportJSF12ProjectFromJBDS4x.PROJECT_TO_IMPORT_NAME
							+ " project ",
					problemsView
							.getProblems(ProblemType.ERROR, new ProblemsPathMatcher(
									new RegexMatcher("." + ImportJSF12ProjectFromJBDS4x.PROJECT_TO_IMPORT_NAME)))
							.size() == 0);
			System.out.println(problemsView.getProblems(ProblemType.WARNING, new ProblemsPathMatcher(
					new RegexMatcher("." + ImportJSF12ProjectFromJBDS4x.PROJECT_TO_IMPORT_NAME))).size());
			assertTrue(
					"https://issues.jboss.org/browse/JBIDE-17422\n" + "There were warnings when importing "
							+ ImportJSF12ProjectFromJBDS4x.PROJECT_TO_IMPORT_NAME
							+ " project ",
					problemsView
							.getProblems(ProblemType.WARNING, new ProblemsPathMatcher(
									new RegexMatcher("." + ImportJSF12ProjectFromJBDS4x.PROJECT_TO_IMPORT_NAME)))
							.size() == 0);
		} catch (IOException ioe) {
			throw new RuntimeException("Unable to copy and unzip necessary files from plugin's resources directory",
					ioe);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void tearDown() throws Exception {
		packageExplorer.open();
		if (packageExplorer.containsProject(PROJECT_TO_IMPORT_NAME)) {
			packageExplorer.getProject(ImportJSF12ProjectFromJBDS4x.PROJECT_TO_IMPORT_NAME).delete(true);
		}
		super.tearDown();
	}
}
